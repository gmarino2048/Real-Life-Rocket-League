var express = require('express');
var app = express();
var server = require('http').Server(app);
const datagram = require('dgram');
var keypress = require('keypress');
var net = require('net');
var client = net.Socket();

keypress(process.stdin);

client.connect(24000, '172.20.20.242', function(){
    client.write('more stuff');
    console.log('test');
});

//listen for the "keypress" event
process.stdin.on('keypress', function (ch, key) {

    if (key && key.ctrl && key.name == 'c') {
        process.stdin.pause();
        console.log('hello');

    }
});

client.on('data', function(data) {
    console.log('Received: ' + data);
    client.destroy(); // kill client after server's response
});

client.on('close', function() {
    console.log('Connection closed');
});


app.get('/', function(request, response){
    response.sendFile(__dirname + '/client/Welcome_Page.html');

});
app.use('/client', express.static(__dirname + '/client'));
server.listen(8080);
console.log("server started...");

var io = require('socket.io')(server,{});
io.sockets.on('connection', function(socket){
    socket.on('keyup', function(){
        console.log('key release');
        client.write('release');

    });

    socket.on('keydown', function(){
        client.write('press');
    })

    socket.on('keyright', function(){
        console.log('keyright');
    })

    socket.on('keyleft', function(){
        console.log('keyleft');
    })
    console.log('socket connection');
});


