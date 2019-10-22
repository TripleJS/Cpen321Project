const router = require('express').Router();
const questionController = require('../../../controller/question');
const {userValidator} = require('../../../utils/validator');


router.get('/questions/get-question', questionController.getQuestion);
router.post('/questions/post-question', userValidator, questionController.postQuestion);
router.get('/questions/suggest', questionController.suggestedQuestions);
router.get('/questions/search', questionController.searchQuestion);

module.exports = router;