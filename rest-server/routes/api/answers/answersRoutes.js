const router = require("express").Router();
const passport = require("passport");
const passportConfig = require("../../../middleware/passport");
const getMostRecentAnswer = require("../../../controller/answer");

router.get("/answers/recent/:userId", getMostRecentAnswer);

module.exports = router;