/*
 * Virtual Calendar
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
 *    2020-11-30  thebearmay	 Original
 * 
 */

metadata {
    definition (
		name: "Virtual Calendar Extension Device", 
		namespace: "thebearmay", 
		author: "Jean P. May, Jr."
	) {
        capability "Actuator"
		
		attribute "inputDate", "string"
		attribute "calWk", "string"
		attribute "dayOfWeekStr", "string"
		attribute "dayOfWeek", "string"
 
		command "configure", []
    }   
}

preferences {
	input("debugEnable", "bool", title: "Enable debug logging?")
}


def updateValues() {
   dayMap=[
       1:"Sunday",
       2:"Monday",
       3:"Tuesday",
       4:"Wednesday",
       5:"Thursday",
       6:"Friday",
       7:"Saturday"]
    dateNow = new Date()
    calWk = dateNow.getAt(Calendar.WEEK_OF_YEAR)	
    sendEvent(name: "calWk", value: calWk)
    dayOfWeek = dateNow.getAt(Calendar.DAY_OF_WEEK)
    sendEvent(name: "dayOfWeek", value: dayOfWeek)
    dayOfWeekStr = dayMap[dayOfWeek]
    sendEvent(name: "dayOfWeekStr", value: dayOfWeekStr)
    
    midnight=dateNow+1
    midnight.clearTime()
    secondsToMidnight = Math.round((midnight.getTime() - new Date().getTime())/1000)+1
    
    if(debugEnable) log.debug "values updated..."
    runIn(secondsToMidnight,updateValues)
}

def configure() {
	updateValues()
}

def installed() {
	log.trace "installed()"
	updateValues()
}

def updated(){
	log.trace "updated()"
	if(debugEnable) runIn(1800,logsOff)
}

void logsOff() {
	log.debug "debug logging disabled..."
	device.updateSetting("debugEnable",[value:"false",type:"bool"])
}
