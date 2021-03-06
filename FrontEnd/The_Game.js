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
 *
 *
 * forward 1
 * wheels right 2
 * reverse 3
 * wheels left 4
 * drive right 5
 * drive left 6
 * reverse right 7
 * reverse left 8
 * brake 9
 * boost 10
 *
 *
 */


var socket = io.connect("127.0.0.1:9000");

socket.emit('game',  JSON.stringify({
    queryType: 'gameCreation',
    player1: '123',
    player2: 'guest'
}));


const newLocal = document.getElementById("buttonQ").onclick = function () {
    location.href = "EndGame.html";
    socket.emit('update', JSON.stringify({
        player: 1,
        command: 11
    }));
};

//Car Code
var car;
var myBackground;

function startGame() {
    car = new component(40, 40, "red_car.png", 540, 120, "image");
    myBackground = new component(880, 470, "soccer_field_311115.jpg", 0, 0, "image");
    arena.start();
    document.getElementById('timer').innerHTML = 01 + ": " + 01;
    startTimer();
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


        });
        window.addEventListener('keyup', function (e) {
            arena.key = false;
        });

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


function startTimer() {

    var presentTime = document.getElementById('timer').innerHTML;
    var timeArray = presentTime.split(/[:]+/);
    var minute = timeArray[0];
    var second = checkSecond((timeArray[1] - 1));
    if (second == 59) {
        minute = minute - 1;
    }
    if (minute == 0 && second == 0) {
        document.getElementById('timer').innerHTML = minute + ":" + second;
        window.location.href = 'EndGame.html';
        return;
    }
    document.getElementById('timer').innerHTML = minute + ":" + second;
    setTimeout(startTimer, 1000);
}

function checkSecond(sec) {
    if (sec < 10 && sec >= 0) {
        sec = "0" + sec
    };
        socket.emit('endGame', {
            queryType: 'endGame',
            player1: '',
            player2: ''
        });
    if (sec < 0) {
        sec = "59"
    };
    return sec;
}
//Creating arena boundaries
/*var xAxis = 880;
var yAxis = 470;

function keyDown(e) {
    
    //up
    if (e.keyCode == 38) {

        arena.clear();
        if (yAxis - 3 > 0) {
            yAxis = yAxis - 3;
        }
        context.drawImage(car, xAxis, yAxis, 880, 370);
    }

    //down
    if (e.keyCode == 40) {
        arena.clear();
        if (yAxis + 3 + 370 < canvas.height) {
            yAxis = yAxis + 3;
        }
        context.drawImage(car, xAxis, yAxis, 880, 370);
    }
    //left
    if (e.keyCode == 37) {
        arena.clear();
        if (xAxis - 10 > 0) {
            xAxis = xAxis - 10;
        }
        context.drawImage(car, xAxis, yAxis, 880, 370);
    }
    //right
    if (e.keyCode == 39) {
        arena.clear();
        if (xAxis + 10 + 880 < canvas.width) {
            xAxis = Axis + 10;
        }
        context.drawImage(car, xAxis, yAxis, 880, 370);
    }
}

function clearCanvas() {
    this.canvas.width = canvas.width;
}*/



/*Key Pressed*/
function updateArena() {
    arena.clear();
    myBackground.newPos();
    myBackground.update();
    car.newPos();
    car.update();
    carWidth = 40;
    car.speedX = 0;
    car.speedY = 0;

    //left arrow
    if (arena.key && arena.key == 37) {
        car.speedX = -1;
        socket.emit('update', JSON.stringify({
            player: 1,
            command: 1
        }));
    }
    //right arrow
    if (arena.key && arena.key == 39) {
        car.speedX = 1;
        socket.emit('update', JSON.stringify({
            player: 1,
            command: 2

        }));
    }
    //up arrow
    if (arena.key && arena.key == 38) {
        car.speedY = -1;
        socket.emit('update', JSON.stringify({
            player: 1,
            command: 3

        }));
    }
    //down arrow
    if (arena.key && arena.key == 40) {
        car.speedY = 1;
        socket.emit('update', JSON.stringify({
            player: 1,
            command: 4

        }));
    }
    //left (a)
    if (arena.key && arena.key == 65) {
        car.speedX = -1;
        socket.emit('update', JSON.stringify({
            player: 1,
            command: 5

        }));
    }
    //right (d)
    if (arena.key && arena.key == 68) {
        car.speedX = 1;
        socket.emit('update', JSON.stringify({
            player: 2,
            command: 0

        }));
    }
    //up (w)
    if (arena.key && arena.key == 87) {
        car.speedY = -1;
        socket.emit('update', JSON.stringify({
            player: 2,
            command: 1

        }));
    }
    //down (s)
    if (arena.key && arena.key == 83) {
        car.speedY = 1;
        socket.emit('update', JSON.stringify({
            player: 1,
            command: 9,

        }));
    }

    //Boost
    if (arena.key && arena.key == 32) {
        socket.emit('update', JSON.stringify({
            player: 2,
            command: 10,

        }));
    }
    car.newPos();
    car.update();
    car.newPos();
    car.update();
}
