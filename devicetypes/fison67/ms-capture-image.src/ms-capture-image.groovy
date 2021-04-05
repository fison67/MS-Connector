/**
 *  MS Capture Image (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2021 fison67@nate.com
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
 

import org.apache.commons.codec.binary.Base64
 
metadata {
	definition (name: "MS Capture Image", namespace: "fison67", author: "fison67", cstHandler: true) {
		capability "Image Capture"
		capability "Refresh"
	}
    
    preferences {
        input name: "portforwardAddress", title:"Port Forwarding Address" , type: "string", required: true
        input name: "videoUrl", title:"Video URL" , type: "string", required: true
    }
}

def installed(){
    def currentTime = new Date()
    sendEvent(name: "encrypted", value: false)
    setImage("https://upload.wikimedia.org/wikipedia/commons/1/14/Updated_SmartThings_Logo.png")
}

def updated(){
	take()
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

// handle commands
def take() {
	log.debug "Executing 'take'"
    
    httpGet("http://${settings.portforwardAddress}/capture?video=${encodeAC(settings.videoUrl)}") { response ->
        setImage(response.data)
    }
}

def refresh(){
	log.debug "refresh"
    take()
}

def encodeAC(arg){
    Base64 coder = new Base64()
    return new String(coder.encodeBase64(arg.getBytes("utf-8"), false), "utf-8")
}

def setImage(url){
    sendEvent(name:"captureTime", value: new Date().format("yyyy-MM-dd'T'HH:mm:ss+09:00", location.timeZone))
    sendEvent(name:"image", value: url)
}
