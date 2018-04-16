var express = require('express');
var http = require('http');
var fs = require('fs');
var app = express();
var server = require('http').Server(app);
var keypress = require('keypress');
var io = require('socket.io').listen(server);
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
keypress(process.stdin);

var db = mongoose.connection;

var tcpIP = '172.19.21.177';
var tcpPort = '9009';



/* ROUTERS
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
*/

/* CONNECTING TO HTML */
//key capture

/* Connect Welcome page to index */
app.get('/', function(request, response){
  response.sendFile(__dirname + '/client/Welcome_Page.html');

});

/* Key presses */
app.get('/The_Game', function(request, response){
    response.sendFile(__dirname + '/client/The_Game.html');
});

app.get('/Login_Page', function(request, response){
    response.sendFile(__dirname + '/client/Login_Page.html');
});

app.get('/Game_Lobby', function(request, response){
    response.sendFile(__dirname + '/client/Game_Lobby.html');
});

app.get('/CarSoccer', function(request, response){
    response.sendFile(__dirname + '/client/CarSoccer.html');
});9

app.use('/client', express.static(__dirname + '/client'));
server.listen(8080, '127.0.0.1');
console.log("server started...");

/* Route pages*/
//app.use('/Welcome_Page', indexRouter);
//app.use('/users', usersRouter);

var urlencodedParser = bodyParser.urlencoded({extended: false});


// Gets info from LOGIN, SEND TO BACKEND TO VERIFY CREDENTIALS
app.post('/', urlencodedParser, function(req, res, next){
    client.connect(9009, '172.20.39.157', function() {
        var file = JSON.stringify(req.body);
        var type = "queryType: 'userInfo'";
        console.log(JSON.stringify(json));
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
    //console.log(req.body);
    next();
    //res.send('/Game_Lobby');
});

app.post('/', urlencodedParser, function(req, res) {
});

/*
/* SOCKETS TO RECEIVE DATA */
/*
var clients = {};
io.sockets.on('connection', function(socket) {
    console.log("New Connection"); //If Verbose Debug
    var userName;
    socket.on('connection name',function(user){
        console.log("Connection Name"); //If Verbose Debug
        userName = user.name;
        clients[user.name] = socket;
        io.sockets.emit('new user', user.name + " has joined.");
    });

    console.log('socket connection');

});


*/

//IP: 172.20.22.6

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
}	));


// Express messages
app.use(require('connect-flash')());
app.use(function (req, res, next) {
    res.locals.messages = require('express-messages')(req, res);
    next();
});