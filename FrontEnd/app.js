var express = require('express');
var http = require('http');
var fs = require('fs');
var app = express();
//var server = http.Server(app);

var server = app.listen(9000, function(){
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
io.on('connection', function(socket){
   console.log('connection made');

   socket.on('update', function(data) {
       console.log(data);
       const dgram = require('dgram');
       const message = new Buffer(data);
       //const message = JSON.stringify(data);
       const client = dgram.createSocket('udp4');
       client.send(message, bytePort, IP, (err) => {
           client.close();
       });

   });

   socket.on('endGame', function(data){
      console.log(data);

       client.connect(dbPort, IP, function() {
           console.log(JSON.stringify(req.body));
           client.write(JSON.stringify(req.body));
       });

       client.on('data', function(data) {
           var info = JSON.parse(data);
           console.log('Received: ' + JSON.stringify(info));

           //if login success, send data to the web
           if (info.queryResult == 'success'){
               res.redirect('/Game_Lobby?token=' + info.username);
           }
           //else send an error page
           else
               res.end('Login failure. Please try again.');
           client.destroy(); // kill client after server's response
       });

       client.on('close', function() {
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

app.get('/', function(request, response){
  response.sendFile(__dirname + '/Website_Code/index.html');
});


/* Key presses */


app.get('/Login_Page', function(request, response){
    response.sendFile(__dirname + '/Website_Code/Login_Page.html');
});

app.get('/Game_Lobby', function(request, response){
    response.sendFile(__dirname + '/Website_Code/Game_Lobby.html');
});

app.get('/CarSoccer', function(request, response){
    response.sendFile(__dirname + '/Website_Code/CarSoccer.html');
});

//app.use('/Website_Code', express.static(__dirname + '/Website_Code'));

app.use(express.static('Website_Code'));

app.use(express.static(path.join(__dirname + '/Website_Code')));

/* Route pages*/
//app.use('/Welcome_Page', indexRouter);
//app.use('/users', usersRouter);

var urlencodedParser = bodyParser.urlencoded({extended: false});

/** WELCOME PAGE GET & POST **/

app.get('/Welcome_Page', function(request, response){
    response.sendFile(__dirname + '/Website_Code/Welcome_Page.html');
});

// Gets info from LOGIN, SEND TO BACKEND TO VERIFY CREDENTIALS
app.post('/Welcome_Page', urlencodedParser, function(req, res){

    client.connect(dbPort, IP, function() {
        console.log(JSON.stringify(req.body));
        client.write(JSON.stringify(req.body));
    });

    client.on('data', function(data) {
        var info = JSON.parse(data);
        console.log('Received: ' + JSON.stringify(info));

        //if login success, send data to the web
        if (info.queryResult == 'success') {
            //res.redirect('/Game_Lobby');
            res.redirect('/Game_Lobby?token=' + info.username);
        }
        else
            res.end('Login failure. Please try again.');
        client.destroy(); // kill client after server's response
    });

    client.on('close', function() {
        console.log('Connection closed');
    });
    console.log(JSON.stringify(req.body));

    //token should be part of the JSON

    //send request to database server

});

//
app.get('/data', function (req, res){

});

/** THE GAME GET & POST REQUESTS **/

app.get('/The_Game', function(request, response){
    response.sendFile(__dirname + '/Website_Code/The_Game.html');
});


app.post('/The_Game', function(request, response){

});

/** ANALYTICS GET & POST REQUESTS **/
app.get('/Analytics', function(request, response){
    response.sendFile(__dirname + '/Website_Code/Analytics.html');

});

//get for /data
app.get('/data', function(request, response){
   var token = request.query.token;
   //query to get the information for specific user
    var message = {
      username: token,
      password: token.toUpperCase()
    };

    console.log(JSON.stringify(message));
     client.connect(dbPort, IP, function() {
           console.log(token);
           client.write(JSON.stringify(message));
       });

     client.on('data', function(data) {
           var info = data;
           console.log('Received: ' + data);
           client.destroy(); // kill client after server's response
       });

     client.on('close', function() {
           console.log('Connection closed');
       });
     console.log(JSON.stringify(req.body));



   //send token to backend to get user data, get json back


    response.json("insert json file");

});


app.get('End_Game', function (request, response){

});