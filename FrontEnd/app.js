var express = require('express');
var http = require('http');
var fs = require('fs');
var app = express();
//var server = http.Server(app);

var server = app.listen(9000, function () {
    console.log("CONNECTED TO SERVER...")
});

//postman for testing

const datagram = require('dgram');
var net = require('net');
var client = net.Socket();
var createError = require('http-errors');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var session = require('express-session');
var passport = require('passport');
var expressValidator = require('express-validator');
var localStrategy = require('passport-local').Strategy;
var multer = require('multer');
var upload = multer({dest: './uploads'});
var flash = require('connect-flash');
var mongo = require('mongodb');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
var stdin = process.openStdin();

var db = mongoose.connection;

var socket = require('socket.io');


var IP = '172.19.38.180';
var dbPort = 9009;
var bytePort = 8001;

//waits for client to make connection
var io = socket(server);
io.on('connection', function (socket) {
    console.log('connection made');

    socket.on('update', function (data) {
        console.log(data);
        var dgram = require('dgram');
        const message = new Buffer(data);
        const client = dgram.createSocket('udp4');
        client.send(message, bytePort, IP, (err) => {
            client.close();
        });


        /**
        var udpserv = dgram.createSocket('udp4');

        udpserv.on('listening', function () {
            var address = udpserv.address();
            console.log('UDP Server listening on ' + address.address + ":" + address.port);
        });

        server.on('message', function (message, remote) {
            console.log(remote.address + ':' + remote.port + ' - ' + message);

        });

        udpserv.bind(9090, 'localhost');

         **/

    });

    socket.on('game', function (data2) {
        client.connect(dbPort, IP, function () {
            console.log(data2);
            client.write(data2);
        });

        client.on('data', function (data2) {
            var info = JSON.parse(data2);
            console.log('Received: ' + JSON.stringify(info));
        });

        client.on('close', function () {
            console.log('Connection closed');
        });
    });
});


/* ROUTERS
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
*/

/* CONNECTING TO HTML */
//key capture

/* Connect Welcome page to index */

app.get('/', function (request, response) {
    response.sendFile(__dirname + '/index.html');
});


/* Key presses */


app.get('/Login_Page', function (request, response) {
    response.sendFile(__dirname + '/Login_Page.html');
});

app.get('/Game_Lobby', function (request, response) {
    response.sendFile(__dirname + '/Game_Lobby.html');
});

app.get('/CarSoccer', function (request, response) {
    response.sendFile(__dirname + '/CarSoccer.html');
});

//app.use('/Website_Code', express.static(__dirname + '/Website_Code'));

app.use(express.static(''));

app.use(express.static(path.join(__dirname + '')));

/* Route pages*/
//app.use('/Welcome_Page', indexRouter);
//app.use('/users', usersRouter);

var urlencodedParser = bodyParser.urlencoded({extended: false});

/** WELCOME PAGE GET & POST **/

app.get('/Welcome_Page', function (request, response) {
    response.sendFile(__dirname + '/Welcome_Page.html');
});

// Gets info from LOGIN, SEND TO BACKEND TO VERIFY CREDENTIALS
app.post('/Welcome_Page', urlencodedParser, function (req, res) {

    client.connect(dbPort, IP, function () {
        client.write(JSON.stringify(req.body));
    });

    client.on('data', function (data) {
        var info = JSON.parse(data);
        console.log('Received: ' + JSON.stringify(info));

        //if login success, send data to the web
        if (info.queryResult == 'success' && (info.queryType == 'userCreation' || info.queryType == 'userInfo')) {
            console.log('here');
            res.redirect('/Game_Lobby.html');
        }
        else {
            res.end('Login failure. Please try again.');
        }
        client.destroy(); // kill client after server's response
    });

    client.on('close', function () {
        console.log('Connection closed');
    });
    //token should be part of the JSON

    //send request to database server

});


/** THE GAME GET & POST REQUESTS **/

app.get('/The_Game', function (request, response) {
    response.sendFile(__dirname + '/The_Game.html');
});


/** ANALYTICS GET & POST REQUESTS **/
app.get('/Analytics', function (request, response) {
    response.sendFile(__dirname + '/Analytics.html');

});


//get for /data
app.get('/data', function (request, response) {
    var token = request.query.token;
    //query to get the information for specific user
    var message = {
        username: token,
        password: token.toUpperCase()
    };

    console.log(JSON.stringify(message));
    client.connect(dbPort, IP, function () {
        console.log(token);
        client.write(JSON.stringify(message));
    });

    client.on('data', function (data) {
        var info = data;
        console.log('Received: ' + data);
        client.destroy(); // kill client after server's response
    });

    client.on('close', function () {
        console.log('Connection closed');
    });
    console.log(JSON.stringify(req.body));


    //send token to backend to get user data, get json back


    response.json("insert json file");

});


app.get('/EndGame', function (request, response) {
    response.end('');
});

app.get('/Final_Game_Stats', function (req, res){


    var json = {
        queryType: 'recentGame',
        player1: '123',
        player2: 'guest'
    };

    //res.end(JSON.stringify(json));
    client.connect(dbPort, IP, function () {
        console.log('test2 game stats');
        console.log(JSON.stringify(json));
        client.write(JSON.stringify(json));
    });

    client.on('data', function (data) {
        var info = JSON.parse(data);

        console.log('Received: ' + JSON.stringify(info));

        var html = '<!DOCTYPE html>'
            + '<html>' + '<head>' +         '<link rel="stylesheet" href="carStyle.css">' + '</head>' +

            '<header>' + '<h1>' + 'Winner: ' + info.winner + '!!!!<h1>' +
            '</header><body><h1><br />' + '\n' + info.player1 + ' score: ' +
            info.player1Score + '<br />' + info.player2 +
            ' score: ' + info.player2Score + '</h1></body></html>';
        res.end(html);
    });

    client.on('close', function () {
        console.log('Connection closed');
    });
});

//try to send json file to FInal Game stats
