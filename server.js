const {port, mongodburl, portRedis} = require("./config");
const {server, startServer} = require("./rest-server/api");
const SocketServer = require("./socket-server/socket");
const redis = require("redis");
const bluebird = require("bluebird");

bluebird.promisifyAll(redis.RedisClient.prototype);
bluebird.promisifyAll(redis.Multi.prototype);

const serverPort = process.env.PORT || port;
const redisPort = process.env.PORT || portRedis;

const redisClient = redis.createClient(redisPort);

const socketServer = new SocketServer(server, redisClient);

startServer(mongodburl, serverPort);
socketServer.startServer(); 
