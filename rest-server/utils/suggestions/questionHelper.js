const Question = require('../../schema/questions');
const User = require('../../schema/user');
const MAX_RETRIEVED_QUESTIONS = 20;


const getQuestions = async (userID) => {
    try {
        let currentUser = await User.findById(userID);

        if (currentUser === null) {
            return null;
        }

        let userQuestions = currentUser.

    } catch {

    }
}