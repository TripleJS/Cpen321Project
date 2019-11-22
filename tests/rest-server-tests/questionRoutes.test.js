const questionController = require("../../rest-server/controller/question");
const mongoose = require('mongoose');
const userController = require("../../rest-server/controller/user");
const User = require("../../rest-server/schema/user");
const Question = require("../../rest-server/schema/questions");
const mockData = require("../mongoose-mock-data");

jest.mock("../../rest-server/utils/errorHandler", () => ({
  errorCatch : jest.fn(),
  errorThrow : jest.fn()
}));

const mockErrorHandler = require("../../rest-server/utils/errorHandler");

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  return res;
};

const next = () => {};

describe("User Route Test Suite", () => {
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

  // Test Suites: 

  describe("Get Question Tests", () => {

    const mockGetQuestionRequest = (id) => {
      return {
        params : {
          questionId : id
        }
      }
    };

    test("Get Existing Question", async () => {
      const res = mockResponse();
      const req = mockGetQuestionRequest(mockData.testQuestion._id);

      await questionController.getQuestion(req, res, next);
      expect(res.status).toHaveBeenCalledWith(200);
      let question = await Question.findById(mockData.testQuestion._id);
      expect(res.json).toHaveBeenCalledWith(question);
    });

    test ("Get non-existing Question", async () => {
      const res = mockResponse();
      const req = mockGetQuestionRequest(mongoose.Types.ObjectId());

      await questionController.getQuestion(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "Could not find Question", 403);
    });

  });

  describe("Post Question Tests", () => {
    test("Posting a new Question", async () => {

    });

    

  });

  describe("Get Most Recent Question Tests", () => {
    test("Get most recent question by existing user", async () => {

    });

    test("Get most recent question by non-existing user", async () => {

    });

  });

  describe("Swiped Question Tests", () => {

    test("Non-exisiting question", async () => {

    });

    test("Existing Question", async () => {

    });

    test("Non-existing user", async () => {

    });
    
    test("Swiping on question user has already swiped on", async () => {

    });
  });
});