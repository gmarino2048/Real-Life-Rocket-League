/*var userRequest = new Request('http://localhost/data');
var params = new URLSearchParams(window.location.search);
window.localStorage.token = params.get(nsc27);

$.ajax({
    url: "http://localhost/data?token=" + window.localStorage.token,
    type: "GET",
    success: function (data) {
        //Display the information
    },
    error: function (data) {
        console.log(data);
    }
});*/
document.getElementById("game").onclick = function () {
    location.href = "Login_Page.html";
}

document.getElementById("logout").onclick = function () {
    location.href = "Index.html";
}
document.getElementById("stats").onclick = function () {
    location.href = "Analytics.html";
}
