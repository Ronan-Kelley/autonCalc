var sequence, distance, angle;
var X, Y;
var sequence = [];
var sequenceP = [];
var isType = -1;

function removeExtraText(str) {
    var str0 = str.replace(/^\s+/, '');
    var str1 = str0.replace(/\t/g, '');
    console.log(str1);
    document.getElementById('debugOut').innerHTML += "<br>"+ str1;
    console.log("removeExtraText: str1 = " + str1)
    if (str1 != undefined) {
        if (str.slice(0, 26) == "addSequential(new DriveTo(") {
            str1 = str.slice(26, -2);
            isType = 1;
            return str1;
        } else if (str.slice(0, 27) == "addSequential(new RotateTo(") {
            str1 = str.slice(27, -2);
            isType = 0;
            return str1;
        } else if (str.slice(0,31) == "addSequential(new RotateDegree(") {
            str1 = str.slice(31, -2);
            isType = 2;
            return str1;
        }
    } else {
        console.log("str1 is undefined");
    }
    // console.log(str1);
}

function parseNums(str) {
    str0 = str;
    if (str0 != undefined) {
        console.log('str0: ' + str0)
        str1 = str0.split(",");
        console.log(str1[0] + "   " + str1[1] + "   " + str1[2]);
        console.log("isType=" + isType);
        if (isType == 1) {
            distance = parseInt(str1[0]);
            angle = parseInt(str1[1]);
        } else if (isType == 0) {
            angle = parseInt(str1[2]);
        } else if (isType == 2) {
            X = parseInt(str1[0]);
            Y = parseInt(str1[1]);
            angle = parseInt(str1[2]);
        }
    } else {
        console.log("str0 is undefined!")
    }
}

// function addToList() {
//     sequence += "sequence.calculateLineEnd(" + distance + ", " + angle + ");";
// }

function genItem(str) {
    parseNums(removeExtraText(str));
    if (isType == 1) {
        return "test.calculateLineEnd(" + distance + ", " + angle + ");";
    } else if (isType == 0) {
        return "test.rotate(" + angle + ");";
    } else if (isType == 2) {
        return "test.rotateDegrees(" + X + ", " + Y + ", " + angle + ");";
    } else {
        console.log("");
        console.log("");
        console.log("error in genItem!");
        console.log("");
        console.log("");
        return "null";
    }
}

function parseLargeString(str) {
    sequence = str.split(";");
    if (sequence[sequence.length - 1].length < 3) {
        delete sequence[sequence.length - 1];
    }
}

function addToArray() {
    for (x = 0; x < sequence.length - 1; x++) {
        sequenceP[x] = genItem(sequence[x]);
        // console.log("sequenceP[" + x + "]: " + sequenceP[x]);
        console.log("isType: " + isType);
    }
}

function addToDoc() {
    var a = document.createElement('script');
    document.body.appendChild(a);
    var b = "function SequenceThis() { ";
    for (x = 0; x < sequence.length - 1; x++) {
        b += sequenceP[x];
    }
    b += " }";
    a.innerHTML += b;
}

var debug = {
    dumpSequence: function () {
        for (x = 0; x < sequence.length - 1; x++) {
            console.log(sequence[x]);
            console.log(' ');
            document.getElementById('debugOut').innerHTML += "<br>" + sequence[x];
        }
    },
    dumpSequenceP: function() {
        for (x = 0; x < sequence.length - 1; x++) {
            console.log(sequenceP[x]);
            console.log(' ');
            document.getElementById('debugOut').innerHTML += "<br>" + sequenceP[x];
        }
    }
}