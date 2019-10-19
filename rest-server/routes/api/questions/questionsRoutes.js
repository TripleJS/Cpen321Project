const router = require('express').Router();
const questionController = require('../../../controller/question');

router.get('/questions/get-question', questionController.getQuestion);
router.post('/questions/post-question', questionController.postQuestion);
router.get('/questions/suggest', questionController.suggestedQuestions);
router.get('/questions/search', questionController.searchQuestion);

module.exports = router;