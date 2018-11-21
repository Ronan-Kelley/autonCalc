var sanatizer = {
    commands: [],
    junkCommands: [],
    str0: "",
    buildCommandsArray: function () {
        str1 = sanatizer.str0.replace(/\n\t/g, ''); //deletes newlines/tabs in the string
        sanatizer.commands = str1.split(";"); //
        if (sanatizer.commands[sanatizer.commands.length - 1].length < 2) {
            delete sanatizer.commands[sanatizer.commands.length - 1];
        }
    },
    reorderCommandsArray: function () {
        /*
         ** if the commands array contains undefined slots, then move them to the end.
         */
        for (x = 0; x < sanatizer.commands.length - 1; x++) {
            if (sanatizer.commands[x] != undefined) {
                sanatizer.junkCommands.push(sanatizer.commands[x]);
            }
        }
    },
    cleanCommandsArray: function () {
        for (x = 0; x < sanatizer.commands.length - 1; x++) {
            if (sanatizer.commands[x].search(/driveto|rotateto|rotatedegree/i) < 2 || sanatizer.commands[x].search(/\/\//) != -1) {
                delete sanatizer.commands[x];
            }
        }
    },
    cleanSpaces: function() {
        var xvar = 0;
        for (x = 0; x < sanatizer.commands.length - 1; x++) {
            // console.log('[cleanSpaces] x: ' + x + ", sanatizer.commands.length-1: " + sanatizer.commands.length - 1);
            INNER_LOOP: for (i = 0; i < sanatizer.commands[xvar].length; i++) {
                // console.log("cleanSpaces: currently running through " + sanatizer.commands[xvar]);
                if (sanatizer.commands[xvar].slice(0, 1) == " ") {
                    sanatizer.commands[xvar] = sanatizer.commands[xvar].replace(/^\s+/, '');
                } else {
                    xvar++;
                    break INNER_LOOP;
                }
            }
        }
    },
    run: function (str) {
        var runOutput = "";
        sanatizer.str0 = str;
        sanatizer.buildCommandsArray();
        for (x = 0; x < sanatizer.commands.length - 1; x++) {
            // console.log(sanatizer.commands[x]);
        }
        sanatizer.cleanSpaces();
        for (x = 0; x < sanatizer.commands.length - 1; x++) {
            // console.log(sanatizer.commands[x]);
        }
        sanatizer.cleanCommandsArray();
        for (x = 0; x < sanatizer.commands.length - 1; x++) {
            // console.log(sanatizer.commands[x]);
        }
        sanatizer.reorderCommandsArray();
        for (x = 0; x < sanatizer.junkCommands.length; x++) {
            // console.log("final junkCommands value " + x + ": " + sanatizer.junkCommands[x]);
            runOutput += sanatizer.junkCommands[x] + ";";
        }
        if (runOutput == "") {
            // console.log("final junkCommands value " + 1 + ": " + sanatizer.junkCommands[0]);
            runOutput += sanatizer.junkCommands[0] + ";";
        }
        console.log(runOutput);
        // document.getElementById('debugOut').innerHTML += "<hr>" + runOutput;
        return runOutput;
    }
}