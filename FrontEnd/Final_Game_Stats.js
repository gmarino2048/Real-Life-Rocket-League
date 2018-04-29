window.setTimeout(function () {
    window.location.href = 'Game_Lobby.html';
}, 30000);

$(document).ready(function () {
    $('#final').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": "../server_side/scripts/server_processing.php"
    });
});
