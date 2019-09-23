const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const passport = require('passport');

app.use(passport.initialize());
app.use(bodyParser.json());
app.use('/api',);

module.exports = app; 