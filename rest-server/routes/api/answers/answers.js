const router = require("express").Router();
const {userValidator} = require("../../../utils/validator");
const passport = require("passport");
const passportConfig = require("../../../middleware/passport");


router.get("/answers/recent/:userId");