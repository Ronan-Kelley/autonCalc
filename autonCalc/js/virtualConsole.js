var virtualConsole = {
    targetViewPort: "",
    toggleConsole: function() {
        var port = document.getElementById(virtualConsole.targetViewPort);
        if (port.style.visibility == "hidden") {
            port.style.visibility = "visible";
            port.style.height = "95%";
            port.style.width = "99%";
        } else if (port.style.visibility == "visible" | port.style.visibility == "") {
            // console.log("it's visibile")
            port.style.visibility = "hidden";
            port.style.height = "0";
            port.style.width = "0";
        }
        // console.log(port.style.visibility)
    },
    printLine: function(portOut) {
        port = document.getElementById(virtualConsole.targetViewPort);
        port.innerHTML += "<br>" + portOut;
    },
    print: function(portOut) {
        port = document.getElementById(virtualConsole.targetViewPort);
        port.innerHTML += portOut;
    }
}