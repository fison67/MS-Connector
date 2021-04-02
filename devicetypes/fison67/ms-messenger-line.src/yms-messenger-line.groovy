/**
 *  Line Messenger (v.0.0.3)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
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
import org.apache.commons.codec.binary.Base64

metadata {
	definition (name: "MS Messenger Line", namespace: "fison67", author: "fison67") {
        capability "Speech Synthesis"
        capability "Actuator"
        
        command "sendMessage", ["string", "string"]
	}

	simulator {}

	tiles {
    	multiAttributeTile(name:"status", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.status", key: "PRIMARY_CONTROL") {
				attributeState("status", label:'${currentValue}', backgroundColor:"#00a0dc")
			}
		}
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def setInfo(String app_url) {
	log.debug "${app_url}"
	state.app_url = app_url
}

def speak(text){
	log.debug "Speak :" + text
	sendCommand(makeCommand( ["data":text] ), null)
}

def sendMessage(text, imageURL){
	log.debug "Speak :" + text + ", IMG: " + imageURL
	sendCommand(makeCommand( ["data":text, "info": ["img":encodeAC(imageURL)]] ), null)
}

def updated() {}

def sendCommand(options, _callback){
	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}

def makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/messenger/line/sendMessage",
        "headers": [
        	"HOST": state.app_url,
            "Content-Type": "application/json"
        ],
        "body":body
    ]
    return options
}

/**
* Base64 Encode a String using Apache commons
*/
def encodeAC(arg){
    Base64 coder = new Base64()
    return new String(coder.encodeBase64(arg.getBytes("utf-8"), false), "utf-8")
}
