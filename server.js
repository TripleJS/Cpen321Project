const {port, mongodburl, redisPort} = require("./config");
const {server, startServer} = require("./rest-server/api");
const SocketServer = require("./socket-server/socket");
const redis = require('redis');

const serverPort = process.env.PORT || port;
const redisPort = process.env.PORT || portRedis;

const redisClient = redis.createClient(portRedis);

const socketServer = new SocketServer(server);

startServer(mongodburl, serverPort);
socketServer.startServer(); 
