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
    }
}); 

/** 
 * Finds the Questions based off the userId 
 */
questionSchema.query.byUserId = function(userId) {
    return this.find({owner : userId});
}

/** 
 * Finds the Questions if the question contains a value from array of courses
 */
questionSchema.query.byCourseTag = function(courses) { 

    let allQuestions = [];
    for (var i = 0; i < courses.length; i++) {
        
        // Finds questions that match the current course
        const questions = this.find({course: courses[parseInt(i)]});

        allQuestions.pushValues(questions);
    }

    return allQuestions;
}

module.exports = mongoose.model("Question", questionSchema);