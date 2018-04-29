$(document).ready(function () {
    $('#final').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": "../server_side/scripts/server_processing.php"
    });
});