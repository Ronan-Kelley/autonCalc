function calcArcEndpointMod(x, y, c, c1, degrees, XorY) {
    degrees = degrees * (Math.PI/180);

    var radius = Math.sqrt(((x - c) * (x - c)) + ((y - c1) * (y - c1))),
        B = Math.atan((x - c) / (y - c1)),
        G = degrees - B;

    var triangleOne = {
            hyp: radius,
            shortSide: 0,
            longSide: 0,
            centerAngle: B
        },
        triangleTwo = {
            hyp: radius,
            shortside: 0,
            longSide: 0,
            centerAngle: G
        }

    triangleOne.longSide = triangleOne.hyp * Math.cos(triangleOne.centerAngle);
    triangleOne.shortSide = Math.sqrt((triangleOne.hyp ** 2) - (triangleOne.longSide ** 2));

    triangleTwo.longSide = triangleOne.hyp * Math.cos(triangleTwo.centerAngle);
    triangleTwo.longSide = Math.sqrt((triangleTwo.hyp ** 2) - (triangleTwo.longSide ** 2));

    var newX = Math.round(Math.cos(triangleTwo.centerAngle) * triangleTwo.hyp + c1), //calculate the new X based off of the old line, degrees, and distance
        newY = Math.round(Math.sin(triangleTwo.centerAngle) * triangleTwo.hyp + c1); //calculate the new Y based off of the old line, degrees, and distance


    if (XorY == "X" || XorY == "x") {
        return newX;
    } else if (XorY == "Y" || XorY == "y") {
        return newY;
    }
}


//-------------------------
function calcArcEndpoint() {
// degrees = this.circleDegrees * (Math.PI / 180);
degrees = this.circleDegrees;

var x = this.x1,
    y = this.y1,
    c = this.circleX,
    c1 = this.circleY, // - 781,
    quadrant = this.circleQuadrant,
    finalAngle;

console.log("x: " + x + ", y: " + y + ", c: " + c + ", c1: " + c1);

// var radius = Math.sqrt(((x - c) * (x - c)) + ((y - c1) * (y - c1))),
var radius = this.circleRadius,
    B = toDegrees(Math.atan((x - c) / (y - c1))),
    G = degrees - B;

switch (quadrant) {
    case 1:
        if (270 > this.circleStartAngle + B) {
            diffBnextQuad = 270 - (toDegrees(this.circleStartAngle) + B);
        }
        break;
    case 3:
        if (90 > this.circleStartAngle + B) {
            diffBnextQuad = 90 - (toDegrees(this.circleStartAngle) + B);
        }
        break;
    case 4:
        if (180 > this.circleStartAngle + B) {
            diffBnextQuad = 180 - (toDegrees(this.circleStartAngle) + B);
        }
        break;
}

console.log("B: " + B + ", G: " + G + ", Degrees: " + degrees + ", diffBnextQuad: " + diffBnextQuad);

G = toRadians(G);

switch (quadrant) {
    case 1:
        finalAngle = 0.5 * Math.PI - G;
        finalAngle = (this.circleStartAngle - 1.5 * Math.PI) + finalAngle
        console.log("finalAngle: " + finalAngle);
        break;
    case 2:
        finalAngle = 0.5 * Math.PI - G;
        console.log("finalAngle: " + finalAngle);
        break;
    case 3:
        finalAngle = 0.5 * Math.PI - G;
        finalAngle = (this.circleStartAngle - 0.5 * Math.PI) + finalAngle
        console.log("finalAngle: " + finalAngle);
        break;
    case 4:
        finalAngle = 0.5 * Math.PI - G;
        finalAngle = (this.circleStartAngle - 1 * Math.PI) + finalAngle
        console.log("finalAngle: " + finalAngle);
        break;
}

var triangleOne = {
        hyp: radius,
        shortSide: 0,
        longSide: 0,
        centerAngle: B
    },
    triangleTwo = {
        hyp: radius,
        shortside: 0,
        longSide: 0,
        centerAngle: G
    }

// triangleOne.longSide = triangleOne.hyp * Math.cos(triangleOne.centerAngle);
// triangleOne.shortSide = Math.sqrt((triangleOne.hyp ** 2) - (triangleOne.longSide ** 2));

// triangleTwo.longSide = triangleOne.hyp * Math.cos(triangleTwo.centerAngle);
// triangleTwo.longSide = Math.sqrt((triangleTwo.hyp ** 2) - (triangleTwo.longSide ** 2));

var newX = Math.round(Math.cos(finalAngle) * triangleTwo.hyp + c), //calculate the new X based off of the old line, degrees, and distance
    newY = Math.round(Math.sin(finalAngle) * triangleTwo.hyp - c1 - 10); //calculate the new Y based off of the old line, degrees, and distance

// console.log("finalAngle: " + finalAngle);

console.log("newX: " + newX + ", newY: " + newY)

if (XorY == "X" || XorY == "x") {
    return newX;
} else if (XorY == "Y" || XorY == "y") {
    return newY;
}
}