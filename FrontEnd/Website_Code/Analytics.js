Analytics();

function Analytics() {
    document.getElementById("button").onclick = function () {
        location.href = "Game_Lobby.html";
    };
}
$(document).ready(function () {
    var firstThing = $.ajax({
        type: 'GET',
        url: "/request/firstThing",
        data: {},
        async: false
    });

    var secondThing = $.ajax({
        type: 'GET',
        url: "/request/secondThing",
        data: {},
        async: false
    });

    var parsedFirstThing = JSON.parse(firstThing.responseText);
    var parsedSecondThing = JSON.parse(secondThing.responseText);

    for (var i = 0; i < parsedFirstThing.length; i++) {
        var object = parsedFirstThing[i];


    }

    for (var i = 0; i < parsedSecondThing.length; i++) {
        var object = parsedSecondThing[i];
    }
});
