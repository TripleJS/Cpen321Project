const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const http = require('http');
const mongoose = require('mongoose');
const passport = require('passport');
const apiRoute = require('./routes/createRouter');
const error = require('./controller/error');

const server = http.createServer(app);

app.use(passport.initialize());
app.use(bodyParser.json());
app.use('/api', apiRoute);
app.use(error.errorController);
mongoose.set('useNewUrlParser', true);
mongoose.set('useFindAndModify', false);
mongoose.set('useCreateIndex', true);
mongoose.set('useUnifiedTopology', true);

async function startServer(url, port) {
    try {
        console.log('Trying to connect to mongodb database');
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