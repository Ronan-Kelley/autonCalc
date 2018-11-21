function graphicsAPI() {
	var c = document.getElementById('myCanvas');
	var ctx = document.getElementById('myCanvas').getContext("2d");
	this.b = undefined;
	this.plotCircle = function (x, y, radius, quadrant, arcLength, clockDir) {
		var arcLength1, arcLength2;
		if (quadrant == "undefined" || quadrant == undefined) {
			console.log('no quadrant supplied!');
			quadrant = 1;
		} else {
			if (quadrant == 1) {
				arcLength1 = 1.5;
			} else if (quadrant == 2) {
				arcLength1 = 0;
			} else if (quadrant == 3) {
				arcLength1 = .5;
			} else if (quadrant == 4) {
				arcLength1 = 1;
			}
			console.log(arcLength1);
		}
		if (arcLength == "undefined" || arcLength == undefined) {
			arcLength2 = 2 - arcLength1;
		} else {
			arcLength2 = (arcLength / 180) + arcLength1;
		}
		if (clockDir == "undefined" || clockDir == undefined) {
			clockDir = false;
		}
		console.log(arcLength1 + "," + arcLength2);
		ctx.beginPath();
		ctx.arc(x, y, radius, arcLength1 * Math.PI, arcLength2 * Math.PI, clockDir);
		ctx.stroke();
	}
	this.plotRectangle = function (x, y, x1, y1) {
		ctx.rect(x, y, x1, y1);
		ctx.stroke();
	}
	this.plotRectRotate = function (x, y, degs) {
		var height = 33.5;
        var width = 38.5;
		// ctx.lineWidth = 0;
		// ctx.strokeStyle = "white";
		ctx.globalAlpha = 0;
		ctx.rect(x,y,height,width);
		ctx.stroke();
		// ctx.globalAlpha = 1;
		// translate (including the desired centerX & center)
		ctx.translate(x + 16.25, y + 19.25);
		// rotate
		ctx.rotate((degs * Math.PI) / 180);
		// fill the rect offset by half its size
		ctx.rect(-width / 2, -height / 2, width, height);
		// ctx.lineWidth = 2;
		// ctx.strokeStyle = "blue";
		// unrotate
		ctx.rotate((-degs * Math.PI) / 180);
		// untranslate
		ctx.translate(-x - 16.25, -y - 19.25);
		ctx.stroke();
		// ctx.clearRect(x, y, 2, 2);
		// ctx.stroke();
	}
	this.plotLine = function (x, y, x1, y1) {
		ctx.beginPath();
		ctx.moveTo(x, y);
		ctx.lineTo(x1, y1);
		ctx.arc(x, y, 5, 0, 2 * Math.PI);
		ctx.arc(x1, y1, 5, 0, 2 * Math.PI);
		ctx.stroke();
	}
	this.drawImage = function (img, x, y) {
		ctx.drawImage(img, x, y);
	}
}