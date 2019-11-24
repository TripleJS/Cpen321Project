const mongoose = require("mongoose");
const questionHelper = require("../../../rest-server/utils/suggestions/questionHelper");
const mockData = require("../../mongoose-mock-data");
const Question = require("../../../rest-server/schema/questions");
const User = require("../../../rest-server/schema/user");

describe("Question Helper Test Suite", () => {

    let db;

    beforeAll(async () => {
        try {
        db = await mongoose.connect(process.env.MONGO_URL, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
            useFindAndModify : false
        });
        } catch (error) {
            process.exit();
        }
    });
    
    afterAll(async () => {
        await db.close();
    });

    beforeEach(async () => {
        await mockData.initializeDatabase();
    });

    afterEach(async () => {
        await mongoose.connection.dropDatabase();
    })

    describe("Get Questions By User Helper", () => {

        test("Get Question By User that Exists", async () => {
            let result = await questionHelper.getQuestionsByUser(mockData.testUser._id, 3);
            expect(result.length).toEqual(3);
        });

        test("Get Question for User that does not Exist", async () => {
            expect(questionHelper.getQuestionsByUser(mongoose.Types.ObjectId()))
                .rejects.toEqual("Error Getting Questions");
        });

        test("Get Question For User with no Questions Asked", async () => {
            expect(questionHelper.getQuestionsByUser(mockData.testFacebookUser._id))
                .rejects.toEqual("Error Getting Questions");
        });

        test("Get 1 question from user", async () => {
            let result = await questionHelper.getQuestionsByUser(mockData.testUser._id, 1);
            expect(result.length).toEqual(1);
        });

        test("Get 2 questions from user", async () => {
            let result = await questionHelper.getQuestionsByUser(mockData.testUser._id, 2);
            expect(result.length).toEqual(2);
        });
    });


    describe("Testing Get Keyword Frequency", () => {
        test("Basic Array of keywords", () => {
            const testArr = ["hello", "hello", "hello", "what", "is", "what", "john", "test"];
            let result = questionHelper.getKeywordFrequency(testArr, 2);
            expect(result.length).toEqual(2);
            expect(result).toEqual([{keyword : "hello", freq : 3}, {keyword : "what", freq : 2}]);
        });

        test("Empty Array", () => {
            const testArr = [];
            let result = questionHelper.getKeywordFrequency(testArr, 7);
            expect(result.length).toEqual(0);
            expect(result).toEqual([]);
        });

        test("Array containing all same characters", () => {
            const testArr = ["hello", "hello", "hello", "hello", "hello", "hello"];
            let result = questionHelper.getKeywordFrequency(testArr, 2);
            expect(result.length).toEqual(1);
            expect(result).toEqual([{keyword : "hello", freq : 6}]);
        });

        test("Array containing 1, 2 and 3 similar words", () => {
            const testArr = ["hello", "hello", "what", "hello", "test", "what"];
            let result = questionHelper.getKeywordFrequency(testArr, 3);
            expect(result.length).toEqual(3);
            expect(result).toEqual([{keyword : "hello", freq : 3}, {keyword : "what", freq : 2}, {keyword : "test", freq : 1}]);
        });

        test("Array containing 3 keywords with same frequeny", () => {
            const testArr = ["hello", "what", "john", "what", "hello", "there", "atom", "atom"];
            let result = questionHelper.getKeywordFrequency(testArr, 3);
            expect(result.length).toEqual(3);
            expect(result).toEqual([{keyword : "atom", freq : 2}, {keyword : "hello", freq : 2}, {keyword : "what", freq : 2}]);
        });
    })

    describe("Match Keywords Test", () => {
        test("Keywords that match all of those in Question", async () => {
            let question = await Question.findById(mockData.testQuestionArray[4]._id);
            const keywordList = [{keyword : "xd", freq : 2}];
            let result = questionHelper.matchKeywords(question, keywordList);
            expect(result.length).toEqual(4);
            expect(result).toEqual(["xd", "xd", "xd", "xd"]);
        });

        test("Empty Keywords Array", async () => {
            let question = await Question.findById(mockData.testQuestionArray[4]._id);
            const keywordList = [];
            let result = questionHelper.matchKeywords(question, keywordList);
            expect(result.length).toEqual(0);
            expect(result).toEqual([]);
        });

        test("Keywords array where no keywords are in the Question", async () => {
            let question = await Question.findById(mockData.testQuestionArray[6]._id);
            const keywordList = [{keyword : "notxd", freq : 3}, {keyword : "defnotinthere", freq : 2}];

            let result = questionHelper.matchKeywords(question, keywordList);
            expect(result.length).toEqual(0);
            expect(result).toEqual([]);
        });

        test("Keywords array where some words are in the Question", async () => {
            let question = await Question.findById(mockData.testQuestionArray[6]._id);
            const keywordList = [{keyword : "xd", freq : 3}, {keyword : "last", freq : 2}];

            let result = questionHelper.matchKeywords(question, keywordList);
            expect(result.length).toEqual(2);
            expect(result).toEqual(["last", "xd"]);
        });
    });


    describe("Get bag of Questions Test", () => {

        test("1 Question, Matching Keywords",async  () => {
            const keywords = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
            let question = await Question.findById(mockData.testQuestionArray[6]._id);
            const questionsWithKeywords = [{question : question, keywordsWithFreq : [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}]}];

            let result = questionHelper.getBagOfQuestions(questionsWithKeywords, keywords);
            expect(result.length).toEqual(1);
            expect(result).toEqual([question]);
        });

        test("Empty questions array", () => {
            const keywords = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
            const questionsWithKeywords = [];

            let result = questionHelper.getBagOfQuestions(questionsWithKeywords, keywords);
            expect(result.length).toEqual(0);
            expect(result).toEqual([]);
        });

        test("2 questions, returns both questions", () => {
            const keywords = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
            const question1KeywordFreqArray = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
            const question2KeywordFreqArray = [{keyword : "big", freq : 2}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}]; 

            const questionsWithKeywords = [{question : "testq1", keywordsWithFreq : question1KeywordFreqArray}, 
                                            {question : "test12", keywordsWithFreq : question2KeywordFreqArray}];
            
            let result = questionHelper.getBagOfQuestions(questionsWithKeywords, keywords);
            expect(result.length).toEqual(2);
        });

        test("3 questions, only returns 1", () => {
            const keywords = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
            const question1KeywordFreqArray = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
            const question2KeywordFreqArray = [{keyword : "big", freq : 17}, {keyword : "last", freq : 19}, {keyword : "question", freq : 1}]; 
            const question3KeywordFreqArray = [{keyword : "big", freq : 47}, {keyword : "last", freq : 23}]; 

            const questionsWithKeywords = [{question : "testq1", keywordsWithFreq : question1KeywordFreqArray}, 
                                            {question : "testq2", keywordsWithFreq : question2KeywordFreqArray},
                                            {question : "testq3", keywordsWithFreq : question3KeywordFreqArray}];

            let result = questionHelper.getBagOfQuestions(questionsWithKeywords, keywords);
            expect(result.length).toEqual(1);
        });
    })

});