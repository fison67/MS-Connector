/**
 *  MS Connector (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2019 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
 
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.transform.Field

definition(
    name: "MS Connector",
    namespace: "fison67",
    author: "fison67",
    description: "A Connector between Messenger and ST",
    category: "My Apps",
    iconUrl: "https://png.pngtree.com/element_our/png_detail/20181011/facebook-messenger-social-media-icon-design-template-vector-png_126989.jpg",
    iconX2Url: "https://png.pngtree.com/element_our/png_detail/20181011/facebook-messenger-social-media-icon-design-template-vector-png_126989.jpg",
    iconX3Url: "https://png.pngtree.com/element_our/png_detail/20181011/facebook-messenger-social-media-icon-design-template-vector-png_126989.jpg",
    oauth: true
)

preferences {
   page(name: "mainPage")
}


def mainPage() {
	 dynamicPage(name: "mainPage", title: "Messenger Connector", nextPage: null, uninstall: true, install: true) {
   		section("Request New Devices"){
        	input "address", "string", title: "Server address", required: true, description:"IP:Port. ex)192.168.0.100:30200"
        }
        
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    
    if (!state.accessToken) {
        createAccessToken()
    }
    
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"

    initialize()
    setAPIAddress()
}

def setAPIAddress(){
	def list = getChildDevices()
    list.each { child ->
        try{
            child.setAddress(settings.address)
        }catch(e){
        }
    }
}

def initialize() {
	log.debug "initialize"
    addDevice()
}

def addDevice(){
	
    def list = getChildDevices();
    if(list.size() > 0){
    	return
    }
    
    try{
        def childDevice = addChildDevice("fison67", "Messenger", "ms-connector-messenger", location.hubs[0].id, [
            "label": "Messenger"
        ])    
        childDevice.setInfo(settings.address)
    }catch(err){
        log.error err
    }
}

def authError() {
    [error: "Permission denied"]
}

mappings {
    if (!params.access_token || (params.access_token && params.access_token != state.accessToken)) {
    } else {
    }
}
