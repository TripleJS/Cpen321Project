const Question = require('../../schema/questions');
const User = require('../../schema/user');



const getQuestions = async (userID) => {
    try {
        let currentUser = await User.findById(userID);

        if (currentUser === null) {
            return new Error()
        }

    } catch {
        
    }
}