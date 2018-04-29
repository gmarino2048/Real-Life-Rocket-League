A$(document).ready(function () {
    $('#analytics').DataTable({
        "processing": true,
        "serverSide": true,
        "ajax": "../server_side/scripts/server_processing.php"
    });
});