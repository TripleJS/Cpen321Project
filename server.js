const {port, mongodburl} = require('./config');
const {server, startServer} = require('./rest-server/api');
const SocketServer = require('./socket-server/socket');

const serverPort = process.env.PORT || port;
startServer(mongodburl, serverPort);

const socketServer = new SocketServer(server);
socketServer.startServer(); 
