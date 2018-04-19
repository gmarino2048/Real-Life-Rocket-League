// JavaScript source code
//Time Code
var startTime = -1;
var animationLength = 2000; // Animation length in milliseconds

function doAnimation(timestamp) {
    // Calculate animation progress
    var progress = 0;

    if (startTime == 0) {
        startTime = timestamp;
        socket.emit('endGame', {
           queryType: 'endGame',
           player1: 'P1',
           player2: 'P2'
        });
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
    };

/*function loop() {
    var start = (new Date).getTime(); // get the start time
    run_game_code();
    var end = (new Date).getTime(); // get end time

    var delta = end - start; // gets how long the game code ran for

    var delay = 33.33 - delta // 33.33 is the total delay between loops

    setTimeout(function () { loop() }, delay); // delay the difference to get 33.33 intervals
}*/