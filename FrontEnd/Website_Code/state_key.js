// JavaScript source code
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

var car;
var turnState;
var driveState;
var state;
var boostState;
var myBackground;
var stateMap = new Array()();

function keyStart() {
    driveState = 0; //idleState
    turnState = 0;//idleStates
    display.start();
}
var display = {
    canvas: document.createElement("canvas"), start: function () {
        this.canvas.width = 880;
        this.canvas.height = 470;
        this.context = this.canvas.getContext("2d");

        document.body.insertBefore(this.canvas, document.body.childNodes[0]);
        this.interval = setInterval(updateArena, 20);

        window.addEventListener('keydown', function (e) {
            display.key = e.keyCode;
        })
        window.addEventListener('keyup', function (e) {
            display.key = false;
        })
    },
    clear: function () {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}

//States
function sendStates() {
    //left arrow
    if (display.key && display.key == 37) {
        turnState = -1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //right arrow
    if (display.key && display.key == 39) {
        turnState = 1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //up arrow
    if (display.key && display.key == 38) {
        turnState = 0;
        driveState = 1;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //down arrow
    if (display.key && display.key == 40) {
        turnState = 0;
        driveState = -1;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //left (a)
    if (display.key && display.key == 65) {
        turnState = -1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //right (d)
    if (display.key && display.key == 68) {
        turnState = 1;
        driveState = 0;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //up (w)
    if (display.key && display.key == 87) {
        turnState = 0;
        driveState = 1;
        socket.emit('update', {
            turnState: turnState,
            carSpeedX: car.speedX,
            driveState: driveState
        });
    }
    //down (s)
    if (display.key && display.key == 83) {
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