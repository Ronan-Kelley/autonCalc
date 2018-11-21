function toDegrees(rads) {
    return rads * (180 / Math.PI);
}

function toRadians(degs) {
    return degs * (Math.PI / 180);
}

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
        var ang1 = -ang //- this.supplementaryAng; //(ang + this.supplementaryAng); //changed this to negative since the angle was suddenly way off?
        // if (ang1 <= 270) {
        //     angle = (ang1 - ang1 * 2) + 90;
        // } else if (ang1 > 270) {
        //     angle = (ang1 - ang1 * 2) + 270;
        // }
        console.log("----------");
        console.log("[SequencerIO/1] ang1: " + ang1 + ", supplementaryAng: " + this.supplementaryAng + ", initial angle: " + ang);
        angle = (ang1 + 90) % 360;
        //angle=ang
        console.log("[SequencerIO/2] final angle: " + (angle) + ", raw final angle: " + angle);
        var newX = Math.round(Math.cos(angle * Math.PI / 180) * (distance * 1.192857143) + this.x1); //calculate the new X based off of the old line, degrees, and distance
        var newY = Math.round(Math.sin(angle * Math.PI / 180) * (distance * 1.192857143) + this.y1); //calculate the new Y based off of the old line, degrees, and distance
        // console.log("newX: " + newX + ", newY: " + newY);
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
        this.supplementaryAng += ang;
        console.log("rotateTo set angle to " + ang);
        console.log("----------")
    }
    this.setInitSqProperties = function (x, y, x1, y1) { //sets the initial properties for the sequencer
        //start/end are for the x and y of the lines starting point, and start1/end1 are for the x and y of the lines ending point
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.plotRectangle()
        console.log('complete');
    }
    this.Draw = function () {
        virtualConsole.printLine(this.x1 + ", " + this.y1);
        var y = Math.abs(this.y - 781); //math here is to make it so that the coordinates start at the lower left hand corner of the image, instead of the top left
        var y1 = Math.abs(this.y1 - 781); //791 used to be 785
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
        console.log("----------");
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
    // this.plotRectRotate = function () {
    //     var c = document.getElementById('myCanvas');
    //     var ctx = document.getElementById('myCanvas').getContext('2d');
    // }

    this.circleX = 0;
    this.circleY = 0; //THIS SHOULD ALWAYS BE VISUALLY UNCORRECTED Y
    this.circleDegrees = 0;
    this.circleRadius = 0;
    this.circleQuadrant = 0;
    this.circleStartAngle = 0;
    this.circleEndAngle = 0;
    this.circleArcEndPointFinalAng = 0;
    this.circleNewX = 0;
    this.circleNewY = 0;

    this.setCircleProperties = function (x, y, degrees) {
        this.circleX = this.x1 + x;
        this.circleY = this.y1 + y;
        this.circleDegrees = degrees;
        this.circleRadius = Math.abs(Math.sqrt(((this.x1 - this.circleX) * (this.x1 - this.circleX)) + ((this.y1 - this.circleY) * (this.y1 - this.circleY))));
        if (x > 0 && y > 0) {
            quadrant = 3;
        } else if (x > 0 && y < 0) {
            quadrant = 4;
        } else if (x < 0 && y < 0) {
            quadrant = 1;
        } else if (x < 0 && y > 0) {
            quadrant = 2;
        }
        this.circleQuadrant = quadrant;
    }

    this.calcArcEndPoint = function (XorY) {
        var degrees = this.circleDegrees,
            B = (180 / Math.PI) * Math.atan(Math.abs((this.x1 - this.circleX) / (this.y1 - this.circleY))),
            finalAng = B + degrees;
        if (degrees >= 0) {
            if (this.circleQuadrant == 4) {
                finalAng = (90 - finalAng);
            } else if (this.circleQuadrant == 3) {
                finalAng = (90 - finalAng) + 180;
            } else if (this.circleQuadrant == 2) {
                finalAng = (90 - finalAng) + 90;
            }
        } else if (degrees < 0) {
            finalAng = finalAng + (this.circleQuadrant * 90);
        }

        this.calcArcEndPointFinalAng = finalAng;
        console.log("finalAng: " + finalAng + ", this.circleQuadrant: " + this.circleQuadrant);

        var newX = Math.sin(toRadians(finalAng)) * this.circleRadius + this.circleX,
            newY = Math.cos(toRadians(finalAng)) * this.circleRadius + this.circleY;

        this.circleNewX = newX;
        this.circleNewY = newY;

        if (XorY == "X") {
            return newX;
        } else if (XorY == "Y") {
            return newY;
        }
    }
    this.getStartAngle = function () {
        var triangle = {
            hyp: this.circleRadius,
            adjacentHyp: Math.abs(this.x1 - this.circleX),
            oppositeHyp: Math.abs(this.y1 - this.circleY),
            alpha: 0
        }

        // console.log(triangle.adjacentHyp > triangle.hyp)
        // console.log(Math.acos(triangle.adjacentHyp / triangle.hyp))
        triangle.alpha = Math.acos(triangle.adjacentHyp / triangle.hyp)

        startAngle = triangle.alpha;
        // console.log(startAngle);

        switch (this.circleQuadrant) { //all the values are multiplied by this.circleQuadrant by pi because the trig functions return real radians with pi already factored in
            case 1:
                startAngle += (Math.PI * 1.5);
                break;
            case 2:
                startAngle = startAngle;
                break;
            case 3:
                startAngle += (Math.PI * 0.5);
                break;
            case 4:
                startAngle += (Math.PI * 1);
                break;
        }

        this.circleStartAngle = startAngle;
        return startAngle;
    }
    this.getEndAngle = function () {
        var p2 = {
                x: this.circleX,
                y: this.circleY
            },
            p1 = {
                x: this.x1,
                y: this.y1
            },
            diffX = p1.x - p2.x,
            diffY = p1.y - p2.y,
            radius = this.circleRadius,
            p3 = {
                x: this.calcArcEndPoint("X"),
                y: this.calcArcEndPoint("Y")
            },

            endAngle = Math.atan2(p3.y - p2.y, p3.x - p2.x);

        // console.log("p2.x: " + p2.x + ", p2.y: " + p2.y + ", p3.x: " + p3.x + ", p3.y: " + p3.y);

        // console.log("endAngle: " + endAngle)

        // console.log(p1.x + ", " + p1.y + ", " + p2.x + ", " + p2.y + ", " + p3.x + ", " + p3.y);

        switch (this.circleQuadrant) {
            case 1:
                this.circleEndAngle = Math.abs(endAngle) + (1.5 * Math.PI);
                break;
            case 2:
                this.circleEndAngle = Math.abs(endAngle)
                break;
            case 3:
                this.circleEndAngle = Math.abs(endAngle) + (0.5 * Math.PI);
                break;
            case 4:
                this.circleEndAngle = Math.abs(endAngle) + (1 * Math.PI);
                break;
        }

        // this.circleEndAngle = (this.circleEndAngle + 360) % 360 //this does nothing @DREW @DREW @DREW

        // console.log('this.circleEndAngle: ' + this.circleEndAngle);
    }
    this.rotateDegrees = function (x, y, degrees) {
        // console.log(this.y1);
        if (degrees < 0) {
            degrees = 360 + degrees;
        }
        this.setCircleProperties(x, y, degrees);
        // console.log(this.y1);
        this.getStartAngle();
        // console.log(this.y1);
        this.calcArcEndPoint();
        // console.log(this.y1)
        this.getEndAngle();
        var c = document.getElementById('myCanvas'),
            ctx = document.getElementById('myCanvas').getContext('2d');
        ctx.beginPath();
        ctx.lineWidth = 2;
        ctx.strokeStyle = "purple";
        ctx.moveTo(this.circleX, Math.abs(this.circleY - 781));
        ctx.lineTo(this.x1, Math.abs(this.y1 - 781));
        ctx.moveTo(this.circleX, Math.abs(this.circleY - 781));
        ctx.lineTo(this.calcArcEndPoint("X"), Math.abs(this.calcArcEndPoint("Y") - 781));
        ctx.stroke();
        ctx.closePath();
        ctx.beginPath();
        // console.log("start angle: " + this.circleStartAngle + ", end angle: " + this.circleEndAngle);
        // ctx.arc(this.circleX, (this.circleY - 781) * -1, this.circleRadius, this.circleStartAngle, this.circleEndAngle, false);
        ctx.arc(this.circleX, Math.abs(this.circleY - 781), this.circleRadius, 0, 2*Math.PI)
        ctx.lineWidth = 3;
        ctx.strokeStyle = "orange";
        ctx.stroke();

        // console.log("calcArcEndPointX: " + this.calcArcEndPoint("X") + ", calcArcEndPointY: " + this.calcArcEndPoint("Y"));

        if (x != 0 && y != 0) {
            this.x1 = Math.round(this.circleNewX);
            this.y1 = Math.round(this.circleNewY);
        }
        
        virtualConsole.printLine(this.x1 + ", " + this.y1);
        // this.supplementaryAng += degrees;

    }
}