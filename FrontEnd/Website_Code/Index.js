CarSoccer();
var socket = io.connect("127.0.0.1:4000");


/* window.addEventListener('keydown', function (e) {
    console.log('test');
    socket.emit('update', JSON.stringify({
        key: 'hello'
    });

});
*/

function CarSoccer() {
    document.getElementById("button").onclick = function () {
        location.href = "Welcome_Page.html";

    };
}
