const http = require('http');
const app = require('./rest-server/createExpressApp');
const mongoose = require('mongoose');
const server = http.createServer(app);

async function startServer(url, port) {
    try {
        await mongoose.connect(url, {useNewUrlParser: true});
        server.listen(port, () => console.log('Listening on port: ', port));
    }
    catch (err) {
        console.error(err);
    }
}

module.exports = {
    server,
    startServer
}