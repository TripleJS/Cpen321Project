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
        })
    })

    describe("Match Keywords Test", () => {
        test("Keywords that match all of those in Question", () => {

        });

        test("Empty Keywords Array", () => {

        });

        test("Keywords array where no keywords are in the Question", () => {

        });

        test("Keywords array where some words are in the Question", () => {

        });
    });

});