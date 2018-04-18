var express = require('express');
var http = require('http');
var fs = require('fs');
var app = express();
//var server = http.Server(app);

var server = app.listen(9000, function(){
    console.log("CONNECTED TO SERVER...")
});


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

//waits for client to make connection
var io = socket(server);
io.on('connection', function(socket){
   console.log('connection made');

   socket.on('update', function(data){
       console.log('keypress logged');
   });
});

var tcpIP = '172.20.12.100';
var tcpPort = 9000;

var results;



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
    console.log("hello");
    client.connect(tcpPort, tcpIP, function() {
        console.log(JSON.stringify(req.body));
        client.write(JSON.stringify(req.body));
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

    //sample JSON data
    var myObj = {
        name: "savi",
        job: 'student',
        address: '234D'

    };

    //send JSON to web
    if (false){
        res.redirect('/Game_Lobby');
        res.end(JSON.stringify(myObj));
    }
    else{
        res.end("Login failure. Please try again.");
    }


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

//Handle Sessions
app.use(session({
    secret: 'secret',
    saveUninitialized: true,
    resave: true
}));

//Passport
app.use(passport.initialize());
app.use(passport.session());

//Validator
app.use(expressValidator({
    errorFormatter: function(param, msg, value){
        var namespace = param.split('.'),
            root = namespace.shift(),
            formParam = root;

        while(namespace.length){
            formParam += '[' + namespace.shift() + ']';
        }
        return {
            param: formParam,
            msg: msg,
            value: value
        };
    }
}));

// Express messages
app.use(require('connect-flash')());
app.use(function (req, res, next) {
    res.locals.messages = require('express-messages')(req, res);
    next();
});