module.exports = {
    'Login test': function (client) {
        client
            .url('file:///C:/Users/brieb/OneDrive/Software%20Engineering/Real-Life-Rocket-League/FrontEnd/Welcome_Page.html')
            .setValue('input[name="name"]', 'Brianna Lemon')
            .setValue('input[name="password"], 'Passw0rd18')
            .click('button[type="submit"]')
            .assert.containsText('main', 'Game Page')
            .end();
    }
};
/*// Get the modal
var modal = document.getElementById('id01');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
var modal = document.getElementById('id02');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
document.getElementById("direct").onclick = function () {
    location.href = "Game_Lobby.html";
}
document.getElementById("direct1").onclick = function () {
    location.href = "Login_Page.html";
}*/