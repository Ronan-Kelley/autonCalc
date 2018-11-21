function Sequence() {
    this.currentColor = 0;
    this.colors = ["blue", "black", "red", "green", "orange", "yellow", "purple", "gray", "pink", "SpringGreen"]
    this.x = 0;
    this.x1 = 0;
    this.y = 0;
    this.y1 = 0;
    this.supplementaryAng = 0;
    this.calculateLineEnd = function (distance, ang) {
        //distance is in inches, because that's 1:1 inches:pixels
        var ang1 = -ang - this.supplementaryAng; //(ang + this.supplementaryAng); //changed this to negative since the angle was suddenly way off?
        // if (ang1 <= 270) {
        //     angle = (ang1 - ang1 * 2) + 90;
        // } else if (ang1 > 270) {
        //     angle = (ang1 - ang1 * 2) + 270;
        // }
        console.log("[SequencerIO/1] ang1: " + ang1 + ", supplementaryAng: " + this.supplementaryAng + ", initial angle: " + ang);
        angle = (ang1 + 90) % 360;
        //angle=ang
        console.log("[SequencerIO/2] final angle: " + (-angle - 90));
        var newX = Math.round(Math.cos(angle * Math.PI / 180) * (distance * 1.192857143) + this.x1); //calculate the new X based off of the old line, degrees, and distance
        var newY = Math.round(Math.sin(angle * Math.PI / 180) * (distance * 1.192857143) + this.y1); //calculate the new Y based off of the old line, degrees, and distance
        this.x = this.x1;
        this.y = this.y1;
        this.x1 = newX;
        this.y1 = newY;
        this.Draw();
    }
    this.printEndCoords = function () {
        console.log('[SequencerIO/4] the ending coordinates are ' + this.x1 + ", " + this.y1);
    }
    this.rotate = function (ang) {
        // this.supplementaryAng = (this.supplementaryAng - ang) % 360;
        this.supplementaryAng = ang;
        console.log("rotateTo set angle to " + ang);
        console.log("----------")
    }
    this.setInitSqProperties = function (x, y, x1, y1) { //sets the initial properties for the sequencer
        //start/end are for the x and y of the lines starting point, and start1/end1 are for the x and y of the lines ending point
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        console.log('complete');
    }
    this.Draw = function () {
        var y = ((this.y - 781) * -1); //math here is to make it so that the coordinates start at the lower left hand corner of the image, instead of the top left
        var y1 = ((this.y1 - 781) * -1); //791 used to be 785
        var x = this.x;
        var x1 = this.x1;
        var c = document.getElementById('myCanvas');
        var ctx = document.getElementById('myCanvas').getContext('2d');
        ctx.beginPath();
        ctx.lineWidth = 3;
        ctx.strokeStyle = this.colors[this.currentColor];
        this.currentColor++;
        ctx.moveTo(x, y);
        ctx.lineTo(x1, y1);
        ctx.stroke();
        ctx.closePath();
        this.plotCircle();
        this.plotRectangle();
        console.log("[SequencerIO/3] line drawn from " + this.x + "," + this.y + " to " + this.x1 + "," + this.y1 + ", in color " + this.colors[this.currentColor - 1]);
        console.log("----------")
    }
    this.plotCircle = function () {
        var c = document.getElementById('myCanvas');
        var ctx = document.getElementById('myCanvas').getContext('2d');
        ctx.beginPath();
        ctx.arc(this.x1, ((this.y1 - 781) * -1), 3, 0 * Math.PI, 2 * Math.PI);
        ctx.fillStyle = "skyblue";
        ctx.lineWidth = 1;
        ctx.strokeStyle = "skyblue";
        ctx.fill();
        ctx.stroke();
    }
    this.plotRectangle = function () {
        var c = document.getElementById('myCanvas');
        var ctx = document.getElementById('myCanvas').getContext('2d');
        var x = this.x1 - 16.75;
        var y = ((this.y1 - 781) * -1) - 19.25;
        var height = 33.5;
        var width = 38.5;
        ctx.rect(x, y, height, width);
        ctx.lineWidth = 2;
        ctx.strokeStyle = "blue";
        ctx.stroke();
    }
    this.plotRectRotate = function () {
        var c = document.getElementById('myCanvas');
        var ctx = document.getElementById('myCanvas').getContext('2d');
    }
    this.rotateDegrees = function (x, y, degrees) {
        var c = document.getElementById('myCanvas');
        var ctx = document.getElementById('myCanvas').getContext('2d');
        var xpos = this.x1 + x;
        var ypos = ((this.y1 - 781) * -1) + y;
        var endAngle = degrees * (Math.PI / 180);
        var radius, startAngle, quadrant;
        var a = ypos - ((this.y1 - 781) * -1);
        var b = xpos - this.x;
        console.log("[Sequence.rotateDegrees] a: " + a + ", b: " + b);
        if (x < 0 && y < 0) { //determine quadrant, based on a regular intersecting graph with 4 quadrants, not a circle
            quadrant = 3;
        } else if (x < 0 && y >= 0) {
            quadrant = 4;
        } else if (x >= 0 && y >= 0) {
            quadrant = 1;
        } else if (x >= 0 && y < 0) {
            quadrant = 2;
        }
        startAngle = this.calcCircleIntersect(x, ((y - 781) * -1), "start") * Math.PI;
        endAngle1 = this.calcCircleIntersect(x, ((y - 781) * -1), "end") * Math.PI;
        // if (endAngle >= 0) {
        //     endAngle1 = endAngle + startAngle;
        // } else if (endAngle < 0) {
        //     endAngle1 = endAngle - startAngle;
        // }
        radius = Math.sqrt(a*a + b*b);
        console.log("[Sequence.rotateDegrees] xpos: " + xpos + ", ypos: " + ypos + ", radius: " + radius + ", startAngle: " + (startAngle / Math.PI) + ", endAngle1: " + (endAngle1 / Math.PI));
        ctx.beginPath();
        ctx.arc(xpos, ypos, radius, startAngle, endAngle1, false);
        // ctx.arc(xpos, ypos, radius, endAngle1, startAngle, true);
        // ctx.arc(xpos, ypos, radius, 1*Math.PI, startAngle, false);
        // ctx.arc(xpos, ypos, radius, 0*Math.PI, 2*Math.PI);
        ctx.lineWidth = 3;
        ctx.strokeStyle = "orange";
        ctx.stroke();
    }
    this.calcCicleEdgeLength = function(radius, degrees) {
        return 2 * Math.PI * radius * (degrees / 360);
    }
    this.calcCircleIntersect = function(x, y, returnType) {
        var quadrant;
        var correctedY = (this.y1 - 781) * -1;
        var pointA = this.x1;
        var pointA1 = this.y1;
        var pointB = x;
        var pointB1 = y;
        var pointC = this.x1;
        var pointC1 = y;
        var Clength = Math.sqrt(((pointB - pointA) * (pointB - pointA)) + ((pointB1 - pointA1) * (pointB1 - pointA1)));
        var Blength = Math.sqrt(((pointA - pointC) * (pointA - pointC)) + ((pointA1 - pointC1) * (pointA1 - pointC1)));
        var Alength = Math.sqrt(((pointC - pointB) * (pointC - pointB)) + ((pointC1 - pointB1) * (pointC1 - pointB1)));
        var diffX = pointA - pointB;
        var diffY = pointA1 - pointB1;
        var radius = Math.abs(Math.sqrt(diffX*diffX + diffY*diffY)),
        startAngle = Math.atan2(diffY, diffX),
        endAngle = Math.atan2(pointB1 - pointC1, pointB - pointC);
        // var angleA = Math.atan2(pointA,pointA1);
        var angleB = Math.atan2(pointB, pointB1);
        // angleB = (Math.PI/180)*(90-(angleB*(180/Math.PI)));
        // angleB = angleB / Math.PI;//it's still not in radians - just in the number of PIs. DON'T MULTIPLY UNTIL IT IS TIME TO GRAPH
        // console.log(angleB * (180/Math.PI));
        if (x > 0 && y > 0) {
            quadrant = 4;
        } else if (x > 0 && y < 0) {
            quadrant = 3;
        } else if (x < 0 && y < 0) {
            quadrant = 2;
        } else if (x < 0 && y > 0) {
            quadrant = 1;
        }
        console.log("[Sequence.calcCircleIntersect] quadrant: " + quadrant + ", angleB: " + angleB + ", X: " + x + ", Y: "+ y);
        if (returnType == "start") {
            switch(quadrant) {
                case 1:
                    // angleB = angleB / Math.PI;
                    // console.log(angleB + 1.5);
                    // return angleA + 1.5;
                    return startAngle + 1.5;
                    break;
                case 2:
                    // return angleA + 0;
                    return startAngle;
                    break;
                case 3:
                    // return angleA + 0.5;
                    return startAngle + 0.5;
                    break;
                case 4:
                    // return angleA + 1;
                    return startAngle + 1;
                    break;
            }
        } else if (returnType == "end") {
            switch(quadrant) {
                case 1:
                    // angleB = angleB / Math.PI;
                    // console.log(angleB + 1.5);
                    // return angleB + 1.5;
                    return endAngle + 1.5;
                    break;
                case 2:
                    // return angleB + 0;
                    return endAngle;
                    break;
                case 3:
                    // return angleB + 0.5;
                    return endAngle + 0.5;
                    break;
                case 4:
                    // return angleB + 1;
                    return endAngle + 1;
                    break;
            }
        }
    }
}