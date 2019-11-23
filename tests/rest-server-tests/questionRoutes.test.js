const questionController = require("../../rest-server/controller/question");
const mongoose = require('mongoose');
const keywords = require("../../rest-server/utils/lg");
const User = require("../../rest-server/schema/user");
const Question = require("../../rest-server/schema/questions");
const mockData = require("../mongoose-mock-data");

jest.mock("../../rest-server/utils/errorHandler", () => ({
  errorCatch : jest.fn(),
  errorThrow : jest.fn()
}));

jest.mock("../../rest-server/utils/lg", () => ({
  keywords : jest.fn()
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
    const mockPostQuestionRequest = (mockTitle, mockQuestion, mockOwner, mockCourse) => {
      return {
        body : {
          title : mockTitle,
          question : mockQuestion,
          owner : mockOwner,
          course : mockCourse
        }
      }
    }

    test("Posting a new question", async () => {
      const req = mockPostQuestionRequest("new database entry", "some question test", mockData.testUserArray[1]._id, "Cpen 311");
      const res = mockResponse();

      await questionController.postQuestion(req, res, next);
      expect(res.status).toHaveBeenCalledWith(203);
    });

    test("Posting a new question with invalid user", async () => {

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