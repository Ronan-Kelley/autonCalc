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
    startAngle = this.calcCircleIntersect(x, ((y - 781) * -1), "start", degrees) * Math.PI;
    endAngle1 = this.calcCircleIntersect(x, ((y - 781) * -1), "end", degrees) * Math.PI;
    // if (endAngle >= 0) {
    //     endAngle1 = endAngle + startAngle;
    // } else if (endAngle < 0) {
    //     endAngle1 = endAngle - startAngle;
    // }
    radius = Math.sqrt(a * a + b * b);
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
this.calcCircleEndPoint = function (x, y, degrees, radius, XorY) {
    x1 = Math.abs(this.x1);
    y1 = Math.abs(this.y1);
    if (y - y1 < 0 || y - y1 > 0) {
        x1 = x1 + radius * (1 - Math.cos(degrees * (Math.PI / 180)));
        y1 = y1 + (radius * Math.sin(degrees * (Math.PI / 180)));
    }
    x1 = x1 - radius * Math.cos(degrees);
    y1 = y1 + radius * Math.sin(degrees);
    if (XorY == "X") {
        return x1;
    } else if (XorY == "Y") {
        return y1;
    }
}
this.calcCircleIntersect = function (x, y, returnType, degrees) {
    var p2 = {
            x: x,
            y: y
        },
        p1 = {
            x: this.x1,
            y: ((this.y1 - 781) * -1)
        },
        diffX = p1.x - p2.x,
        diffY = p1.y - p2.y,
        radius = Math.abs(Math.sqrt(diffX * diffX + diffY * diffY)),
        p3 = {
            x: this.calcCircleEndPoint(x, y, degrees, radius, "X"),
            y: this.calcCircleEndPoint(x, y, degrees, radius, "Y")
        },
        startAngle = Math.atan2(diffY, diffX),
        endAngle = Math.atan2(p3.y - p2.y, p3.x - p2.x);
    // ctx = document.querySelector("canvas").getContext("2d");

    console.log(p1.x + ", " + p1.y + ", " + p2.x + ", " + p2.y + ", " + p3.x + ", " + p3.y);

    // // arc
    // ctx.arc(p2.x, p2.y, radius, startAngle, endAngle, false);
    // ctx.stroke();

    // // points / lines helpers:
    // ctx.fillRect(p1.x - 2, p1.y - 2, 4, 4);
    // ctx.fillRect(p2.x - 2, p2.y - 2, 4, 4);
    // ctx.fillRect(p3.x - 2, p3.y - 2, 4, 4);
    // ctx.beginPath();
    // ctx.moveTo(p1.x, p1.y);
    // ctx.lineTo(p2.x, p2.x);
    // ctx.lineTo(p3.x, p3.x);
    // ctx.strokeStyle = "orange";
    // ctx.stroke();
    if (x > 0 && y > 0) {
        quadrant = 4;
    } else if (x > 0 && y < 0) {
        quadrant = 3;
    } else if (x < 0 && y < 0) {
        quadrant = 2;
    } else if (x < 0 && y > 0) {
        quadrant = 1;
    }
    angleB = undefined;
    console.log("[Sequence.calcCircleIntersect] quadrant: " + quadrant + ", angleB: " + angleB + ", X: " + x + ", Y: " + y);
    if (returnType == "start") {
        switch (quadrant) {
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
        switch (quadrant) {
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
};