// JavaScript source code
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