const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const questionSchema = new Schema({
    question : {
        type : String,
        required : true
    }, 
    title : {
        type : String,
        required : true
    },
    course : {
        type : String,
        required : true
    }, 
    keywords : {
        type : [String],
        required : true,
        default: []
    }, 
    answers : {
        type : [mongoose.Schema.Types.ObjectId],
        required : true,
        default: []
    },
    answerers : { 
        type : [mongoose.Schema.Types.ObjectId],
        required : true,
        default: []
    },
    owner : {
        type : mongoose.Schema.Types.ObjectId,
        required: true
    },
    rate : {
        type : Number,
        default: 0
    },
    date : {
        type : Date,
        required : true
    },
    swipedUsers : {
        type : [mongoose.Schema.Types.ObjectId],
        ref: "Users",
        required : true,
        default: []
    }
}); 

/** 
 * Finds the Questions based off the userId (Owner)
 */
questionSchema.query.byUserId = function(userId) {
    return this.find({owner : userId});
}

/** 
 * Finds the Questions if the question contains a value from array of courses
 */
questionSchema.query.byCourseTag = function(courses, userId) { 

    let allQuestions = [];
    for (var i = 0; i < courses.length; i++) {
        
        // Finds questions that match the current course and the user hasn't swiped on it yet 
        const questions = this.find({course: courses[parseInt(i)], swipedUsers : {$ne: userId}});

        allQuestions.pushValues(questions);
    }

    return allQuestions;
}

/** 
 * Finds the Questions that a user has swiped on
 */
questionSchema.query.bySwipedUser = function(userId) {
    return this.find({swipedUsers : {$ne: userId}});
}

/** 
 * Finds the Questions that a user has most recently posted
 */
questionSchema.query.mostRecentUserQuestion = function(userId) {
    return this.findOne({owner: userId}, {sort: {date : -1}});
}

module.exports = mongoose.model("Question", questionSchema);