const express = require("express");
const router = express.Router();
const userRoute = require("./api/user/userRoutes");
const questionRoute = require("./api/questions/questionsRoutes");
const answerRoute = require("./api/answers/answersRoutes");
// Set CORS Header info 
router.use((req, res, next) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE");
    res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    next();
});

router.use(userRoute);
router.use(questionRoute);
router.use(answerRoute);

module.exports = router; 