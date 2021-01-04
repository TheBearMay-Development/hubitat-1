 /*
 * Dynamic Attribute Device
 *
 *  Licensed Virtual the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Change History:
 *
 *    Date        Who            What
 *    ----        ---            ----
 *    2021-01-04  thebearmay	 Original version 0.1.0
 * 
 */

static String version()	{  return '0.1.0'  }

metadata {
    definition (
		name: "Dynamic Attribute Device", 
		namespace: "thebearmay", 
		author: "Jean P. May, Jr.",
	        importURL:"https://raw.githubusercontent.com/thebearmay/hubitat/main/dynAttribute.groovy"
	) {
        	capability "Actuator"
		
    attribute "attrRet", "HashMap"
  
    command "setAttribute", [[name:"attrName*", type:"STRING", description:"Attribute Name"],[name:"attrValue*", type:"STRING", description:"Attribute Value"]]   
    command "clearStateVariables"
    }   
}

preferences {
	input("debugEnable", "bool", title: "Enable debug logging?")
}

def installed() {
	log.trace "installed()"
}

def updated(){
	log.trace "updated()"
	if(debugEnable) runIn(1800,logsOff)
}

def setAttribute(attrName,attrValue) {
    if(debugEnable) log.debug "setAttribute($attrName, $attrValue)"
    
    state[attrName]=attrValue
    attrRet = new HashMap(state)
    if(debugEnable) log.debug attrRet
    sendEvent(name:"attrRet", value:attrRet)

}

def clearStateVariables(){
     state.clear()   
}

void logsOff(){
     device.updateSetting("debugEnable",[value:"false",type:"bool"])
}
