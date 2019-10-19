const {port, mongodburl} = require('./config');
const {server, startServer} = require('./rest-server/api');
// const {initSocket} = require('./socket-server/socket.io');

startServer(mongodburl, port);
// initSocket(server);