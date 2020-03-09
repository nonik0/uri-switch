preferences {
	section("Request Info"){
		input "ip", "text", title: "Internal IP", required: true
		input "port", "text", title: "Internal Port", required: true
                input "path", "text", title: "Internal On Path (/path?key=value)", required: true
	}
}

metadata {
	definition (name: "URI Switch", namespace: "nonik0", author: "Nick Brown") {
		capability "Switch"      
                command "toggle"
	}

	tiles {
		standardTile("toggle", "device.button", width: 1, height: 1) {
			state "default", label: "Toggle", backgroundColor: "#ffffff", action: "toggle", icon:""
		}
	}
}

def toggle() { 
    def hostHex = convertIPtoHex(ip)
    def portHex = convertPortToHex(port)
    device.deviceNetworkId = "$hostHex:$portHex" 
    log.debug device.deviceNetworkId
    
    def hubAction = new physicalgraph.device.HubAction(
        'method': 'GET',
        'path': path,
        'headers': [ HOST: "$ip:$port" ]) 
    log.debug hubAction
    
    hubAction
}

private String convertIPtoHex(ipAddress) { 
    return ipAddress.tokenize('.').collect { String.format('%02X', it.toInteger()) }.join()
}

private String convertPortToHex(port) {
	return port.toString().format('%04X', port.toInteger())
}
