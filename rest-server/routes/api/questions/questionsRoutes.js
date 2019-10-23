const router = require('express').Router();
const questionController = require('../../../controller/question');
const {userValidator} = require('../../../utils/validator');


router.get('/questions/get-question/:questionId', questionController.getQuestion);
router.post('/questions/post-question', userValidator, questionController.postQuestion);
router.get('/questions/suggest/:userId', questionController.suggestedQuestions);
router.get('/questions/search', questionController.searchQuestion);
router.post('/questions/swipe', questionController.swipedQuestion);



module.exports = router;