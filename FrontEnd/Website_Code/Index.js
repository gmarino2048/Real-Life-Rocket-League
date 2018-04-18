CarSoccer();
var socket = io.connect("127.0.0.1:9000");

function CarSoccer() {
    document.getElementById("button").onclick = function () {
        location.href = "Welcome_Page.html";

    };
}
