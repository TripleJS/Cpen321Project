const router = require("express").Router();
const questionController = require("../../../controller/question");
const searchQuestion = require("../../../controller/search");
const {userValidator} = require("../../../utils/validator");
const passport = require("passport");
const passportConfig = require("../../../middleware/passport");

router.get("/questions/get-question/:questionId", questionController.getQuestion);
router.post("/questions/post-question", userValidator, questionController.postQuestion);
router.get("/questions/suggest/:userId", passport.authenticate("jwt", {session: false}), questionController.suggestedQuestions);
router.get("/search/:searchQuery", questionController.searchQuestion);
router.post("/questions/swipe", questionController.swipedQuestion);
router.get("/questions/recent/:userId", questionController.getMostRecentQuestion);



module.exports = router;