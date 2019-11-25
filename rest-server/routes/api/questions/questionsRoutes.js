const router = require("express").Router();
const questionController = require("../../../controller/question");
const searchQuestion = require("../../../controller/search");
const passport = require("passport");
const passportConfig = require("../../../middleware/passport");

router.get("/questions/get-question/:questionId", questionController.getQuestion);
router.post("/questions/post-question", questionController.postQuestion);
router.get("/questions/suggest/:userId", passport.authenticate("jwt", {session: false}), questionController.suggestedQuestionsV2);
router.get("/search", searchQuestion);
router.post("/questions/swipe", questionController.swipedQuestion);
router.get("/questions/recent/:userId", questionController.getMostRecentQuestion);

module.exports = router;