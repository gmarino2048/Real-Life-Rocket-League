$(document).ready(function () {

	/**
	 * call the data.php file to fetch the result from db table.
	 */
    $.ajax({
        url: "http://localhost/chartjs2/api/data.php",
        type: "GET",
        success: function (data) {
            console.log(data);

            var score = {
                RedCar: [],
                BlueCar: []
            };

            var len = data.length;

            for (var i = 0; i < len; i++) {
                if (data[i].team == "RedCar") {
                    score.RedCar.push(data[i].score);
                }
                else if (data[i].team == "BlueCar") {
                    score.BlueCar.push(data[i].score);
                }
            }

            //get canvas
            var ctx = $("#line-chartcanvas");

            var data = {
                labels: ["Game1", "Game2", "Game3", "Game4", "Game5"],
                datasets: [
                    {
                        label: " Red score",
                        data: score.RedCar,
                        backgroundColor: "blue",
                        borderColor: "lightblue",
                        fill: false,
                        lineTension: 0,
                        pointRadius: 5
                    },
                    {
                        label: "Blue score",
                        data: score.BlueCar,
                        backgroundColor: "green",
                        borderColor: "lightgreen",
                        fill: false,
                        lineTension: 0,
                        pointRadius: 5
                    }
                ]
            };

            var options = {
                title: {
                    display: true,
                    position: "top",
                    text: "Line Graph",
                    fontSize: 18,
                    fontColor: "#111"
                },
                legend: {
                    display: true,
                    position: "bottom"
                }
            };

            var chart = new Chart(ctx, {
                type: "line",
                data: data,
                options: options
            });

        },
        error: function (data) {
            console.log(data);
        }
    });
}