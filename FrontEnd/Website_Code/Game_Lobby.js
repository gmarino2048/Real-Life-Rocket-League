var userRequest = new Request('http://localhost/data');
var params = new URLSearchParams(window.location.search);
window.localStorage.token = params.get(token);

$.ajax({
    url: "http://localhost/data?token=" + window.localStorage.token,
    type: "GET",
    success: function (data) {


    },
    error: function (data) {
        console.log(data);
    }
});

document.getElementById("game").onclick = function () {
    location.href = "Login_Page.html";
}

document.getElementById("settings").onclick = function () {
    location.href = "Settings.html";
}
document.getElementById("stats").onclick = function () {
    location.href = "Analytics.html";
}