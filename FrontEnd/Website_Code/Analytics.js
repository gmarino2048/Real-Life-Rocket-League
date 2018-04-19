Analytics();

function Analytics() {
    document.getElementById("button").onclick = function () {
        location.href = "Game_Lobby.html";
    };
}
$.ajax({
    url: "http://localhost/data?token=" + window.localStorage.token,
    type: "GET",
    success: function (data) {


        //Display the information


    },
    error: function (data) {
        console.log(data);
    }
});
