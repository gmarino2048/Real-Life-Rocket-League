/*
 * State map: 
 * 0, Neutral - driveState = 0, turnState = 0
 * 1, Forward (Up) - driveState = 1, turnState = 0
 * 2, Right - driveState = 0, turnState = 1
 * 3, Reverse - driveState = 2. turnState = 0
 * 4, Left - driveState = 0, turnState = 2
 * 5, Forward+Right - driveState = 1, turnState = 1
 * 6, Forward+Left - driveState = 1, turnState = -1
 * 7, Reverse+Right - driveState = 2, turnState = 1
 * 8, Reverse+Left - driveState = 2, turnState = 2
 * 9, Brake - driveState = 0, turnState = 0
 * 10, Boost - boostState = 1
 */


var socket = io.connect("127.0.0.1:9000");

const newLocal = document.getElementById("buttonQ").onclick = function () {
    location.href = "EndGame.html";
};
//Car Code
var car;
var turnState;
var driveState;
var state;
var boostState;
var myBackground;
var stateMap= new Array()();
function startGame() {
    car = new component(40, 40, "red_car.png", 540, 120, "image");
    myBackground = new component(880, 470, "soccer_field_311115.jpg", 0, 0, "image");
    states(); //idleState
    arena.start();

    function states() {
        driveState = 0; //idleState
        turnState = 0;//idleStates
    }
}
var arena = {
    canvas: document.createElement("canvas"), start: function () {
        this.canvas.width = 880;
        this.canvas.height = 470;
        this.context = this.canvas.getContext("2d");

        document.body.insertBefore(this.canvas, document.body.childNodes[0]);
        this.interval = setInterval(updateArena, 20);

        window.addEventListener('keydown', function (e) {
            arena.key = e.keyCode;
        })
        window.addEventListener('keyup', function (e) {
            arena.key = false;
        })
    },
    clear: function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}
function component(width, height, color, x, y, type) {
    this.type = type;
    if (type == "image") {
        this.image = new Image();
        this.image.src = color;
    }
    this.area = arena
    this.width = width;
    this.height = height;
    this.speedX = 0;
    this.speedY = 0;
    this.x = x;
    this.y = y;
    this.update = function () {
        ctx = arena.context;
        if (type == "image") {
            ctx.drawImage(this.image, this.x, this.y, this.width, this.height);
        } else {
            ctx.fillStyle = color;
            ctx.fillRect(this.x, this.y, this.width, this.height);
        }
    }
    this.newPos = function () {
        this.x += this.speedX;
        this.y += this.speedY;
    }
}

/*Key Pressed*/
function updateArena() {
    arena.clear();
    myBackground.newPos();
    myBackground.update();
    car.newPos();
    car.update();
    car.speedX = 0;
    car.speedY = 0;

    //If key is released or no key is pressed return to neutral state



    //left arrow
    if (arena.key && arena.key == 37) {
        car.speedX = -1;
    }
    //right arrow
    if (arena.key && arena.key == 39) {
        car.speedX = 1;
    }
    //up arrow
    if (arena.key && arena.key == 38) {
        car.speedY = -1;
    }
    //down arrow
    if (arena.key && arena.key == 40) {
        car.speedY = 1;
    }
    //left (a)
    if (arena.key && arena.key == 65) {
        car.speedX = -1;
    }
    //right (d)
    if (arena.key && arena.key == 68) {
        car.speedX = 1;
    }
    //up (w)
    if (arena.key && arena.key == 87) {
        car.speedY = -1;
    }
    //down (s)
    if (arena.key && arena.key == 83) {
       car.speedY = 1;
    }
    car.newPos();
    car.update();

    car.newPos();
    car.update();
}

//States
function sendStates() {
    //left arrow
    if (arena.key && arena.key == 37) {
        turnState = -1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //right arrow
    if (arena.key && arena.key == 39) {
        turnState = 1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //up arrow
    if (arena.key && arena.key == 38) {
        turnState = 0;
        driveState = 1;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //down arrow
    if (arena.key && arena.key == 40) {
        turnState = 0;
        driveState = -1;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //left (a)
    if (arena.key && arena.key == 65) {
        turnState = -1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //right (d)
    if (arena.key && arena.key == 68) {
        turnState = 1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //up (w)
    if (arena.key && arena.key == 87) {
        turnState = 0;
        driveState = 1;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //down (s)
    if (arena.key && arena.key == 83) {
        turnState = 0;
        driveState = -1;
        socket.emit('update', {
            turnState: turnState,
            rCarSpeedX: car.speedX,
            driveState: driveState
        });
    }
}

//Send the state to the backend
function stateMap() {
    stateMap[0][0] = 9;
    stateMap[1][0] = 1;
    stateMap[0][1] = 2;
    stateMap[2][0] = 3;
    stateMap[0][2] = 4;
    stateMap[1][1] = 5;
    stateMap[1][2] = 6;
    stateMap[2][1] = 7;
    stateMap[2][2] = 8;
}
function updateState() {
    if (boostState) {
        state = 10;
        boostState = 0;
    }
    else {
        state = stateMap[driveState][turnState];
    }
}

/*
//Boost Code
    var fill = true;
    var percentage = 0;
    var increase = 10;
    var max = 100;
    
function boost() {
 //Load Boost
    if (fill == true && <= max) {
        percentage += increment
        if (value == ceiling) {
            fill = false;
        }
//Using boost
    } else {
        fill = false;
        percentage -= increase;

        if (percentage == 0) {
            fill = true;
        }
    }
    document.getElementById('counter').innerHTML = percentage;
}
setInterval(boost, 1000);

//Time Code
var startTime = -1;
var animationLength = 2000; // Animation length in milliseconds

function doAnimation(timestamp) {
    // Calculate animation progress
    var progress = 0;

    if (startTime < 0) {
        startTime = timestamp;
    } else {
        progress = timestamp - startTime;
    }

    // Do animation ...
    if (progress < animationLength) {
        window.requestAnimationFrame(doAnimation);
    }
}

// Start animation
window.requestAnimationFrame(doAnimation);

function redraw() {
    drawPending = false;
    // Do drawing ...
}

var drawPending = false;
function requestRedraw() {
    if (!drawPending) {
        drawPending = true;
        window.requestAnimationFrame(redraw);
    }
}
animator.requestAnimationFrame =
    function (callback) {
        window.requestAnimationFrame(function (t) {
            callback(t);
            redraw();
        });
    };*/