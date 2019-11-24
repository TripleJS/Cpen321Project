const questionController = require("../../rest-server/controller/question");
const mongoose = require('mongoose');
const User = require("../../rest-server/schema/user");
const Question = require("../../rest-server/schema/questions");
const Answer = require("../../rest-server/schema/answers");
const mockData = require("../mongoose-mock-data");

jest.mock("../../rest-server/utils/errorHandler", () => ({
  errorCatch : jest.fn(),
  errorThrow : jest.fn()
}));


const mockErrorHandler = require("../../rest-server/utils/errorHandler");
const mockKeywords = require("../../rest-server/utils/lg");

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  return res;
};

const next = () => {};

describe("Question Route Test Suite", () => {
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

  // Test Suites: 

  describe("Get Answer Tests", () => {

    const mockGetAnswerRequest = () => {
        
    };


    test("Get Answer for Recent Question", async () => {
        
    });

    test("Get Answer for question that does not exist", async () => {

    });

    test("Get Answer for question that does not exist", async () => {

    });

  });

});