const mongoose = require("mongoose");
const questionHelper = require("../../../rest-server/utils/suggestions/questionHelper");
const mockData = require("../../mongoose-mock-data");
const Question = require("../../../rest-server/schema/questions");
const User = require("../../../rest-server/schema/user");

describe("Question Helper Test Quite", () => {

    let db;

    beforeAll(async () => {
        try {
        db = await mongoose.connect(process.env.MONGO_URL, {
            useNewUrlParser: true,
            useUnifiedTopology: true
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

        });

        test("Get Question for User that does not Exist", async () => {

        });

        test("Get Question For User with no Questions Asked", async () => {

        });
    });


    describe("Testing Get Keyword Frequency", () => {
        test("Basic Array of keywords", () => {

        });

        test("Empty Array", () => {

        });

        test("Array containing all same characters", () => {

        });

        test("Array containing 1, 2 and 3 similar words", () => {

        });
    });

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