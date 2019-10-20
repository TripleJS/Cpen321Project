const {port, mongodburl} = require('./config');
const {server, startServer} = require('./rest-server/api');
// const {initSocket} = require('./socket-server/socket.io');
const serverPort = process.env.PORT || port;
startServer(mongodburl, serverPort);
// initSocket(server);