const express = require('express');
const router = express.Router();

// Set CORS Header info 
router.use((req, res, next) =>
{
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, PATCH, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    next();
})
const errorRoute = require('./error');
const registerRoute = require('./register');
const authRoute = require('./auth');

router
    .use()