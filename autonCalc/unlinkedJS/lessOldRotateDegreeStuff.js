this.calcArcEndPoint = function (x, y, degrees, radius, XorY) {
    quadrant = this.circleQuadrant;
    console.log("[Sequence.calcArcEndPoint] x: " + x + ", y: " + y + ", this.x1: " + this.x1 + ", this.y1: " + this.y1);
    // console.log("[Sequence.calcArcEndPoint] quadrant: " + quadrant);
    x1 = Math.abs(this.x1);
    y1 = Math.abs(this.y1);
    console.log("[Sequence.calcArcEndPoint] y1 stage 1: " + y1);
    if (degrees % 90 != 0) {
        x1 = x1 + radius * (1 - Math.cos(degrees * (Math.PI / 180)));
        y1 = y1 + (radius * Math.sin(degrees * (Math.PI / 180)));
        console.log("[Sequence.calcArcEndPoint] y1 stage 2: " + y1);
    }
    x1 = x1 - radius * Math.cos(degrees * (Math.PI / 180));
    y1 = y1 + radius * Math.sin(degrees * (Math.PI / 180));
    console.log("[Sequence.calcArcEndPoint] y1 stage 3: " + y1);


    //make sure that x1/y1 are correctly above/below zero given the quadrant. quadrant 2 doesn't need checking.
    // if (quadrant == 1) {
    //     x1 = Math.abs(x1);
    // } else if (quadrant == 3) {
    //     y1 = Math.abs(y1);
    //     x1 = x1;// * 2;
    // } else if (quadrant == 4) {
    //     x1 = Math.abs(x1);// * 2;
    //     y1 = Math.abs(y1);
    // }

    console.log("[Sequence.calcArcEndPoint] x1: " + x1 + ", [Sequence.calcArcEndPoint] y1: " + y1);

    if (XorY == "X") {
        return x1;
    } else if (XorY == "Y") {
        return y1;
    }
}
this.calcCircleIntersect = function (x, y, startEnd) {
    //calculates not only circle intersect, but also what the end angle should be based off of that.
    //I can't think of a better name, though.
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
        radius = Math.abs(Math.sqrt(diffX * diffX + diffY * diffY));
        this.circleRadius = radius;
        var p3 = {
            x: this.calcArcEndPoint(this.circleX, this.circleY, this.circleDegrees, this.circleRadius, "X"),
            y: this.calcArcEndPoint(this.circleX, this.circleY, this.circleDegrees, this.circleRadius, "Y")
        },
        startAngle = Math.atan2(diffY, diffX),
        endAngle = Math.atan2(p3.y - p2.y, p3.x - p2.x);
    if (startEnd == "start" || startEnd == "Start") {
        return startAngle;
    } else if (startEnd == "end" || startEnd == "End") {
        return endAngle;
    }
}
this.rotateDegrees = function (x, y, degrees) {
    console.log("----------");
    console.log("----------");
    //this line has to be first so that this.calcCircleIntersect can, y'know, work.
    this.circleDegrees = degrees;

    //calculate what quadrant the arc is being drawn in, relative to the origin of the circle.
    var quadrant;
    if (x > 0 && y > 0) {
        quadrant = 4;
    } else if (x > 0 && y < 0) {
        quadrant = 3;
    } else if (x < 0 && y < 0) {
        quadrant = 2;
    } else if (x < 0 && y > 0) {
        quadrant = 1;
    }

    this.circleQuadrant = quadrant;

    var c = document.getElementById("myCanvas"),
        ctx = document.getElementById('myCanvas').getContext('2d'),
        xpos = this.x1 + x,
        ypos = ((this.y1 - 781) * -1) - y,
        calcArcY = this.y1 + y,
        radius = this.circleRadius;

    this.circleX = xpos;
    this.circleY = ypos;
        
    var startAngle = this.calcCircleIntersect(xpos, ypos, "start"),
        endAngle = this.calcCircleIntersect(xpos, ypos, "end");

    this.x1 = Math.abs(this.calcArcEndPoint(xpos, ypos, degrees, radius, "X"));
    this.y1 = Math.abs(this.calcArcEndPoint(xpos, ypos, degrees, radius, "Y"));

    console.log("this.x1: " + this.x1 + ", this.y1: " + this.y1 + ", this.circleX: " + this.circleX + ", this.circleY: " + (this.circleY - 781) * -1);
    console.log(radius)
    
    ctx.beginPath();
    ctx.arc(xpos, ypos, radius, startAngle, endAngle, false);
    console.log("ctx.arc(" + xpos + ", " + ypos + ", " + radius + ", " + startAngle + ", " + endAngle + ", false)")
    ctx.lineWidth = 3;
    ctx.strokeStyle = "orange";
    ctx.stroke();
    console.log("----------");
}
}