const questionController = require("../../rest-server/controller/question");
const mongoose = require('mongoose');
const User = require("../../rest-server/schema/user");
const Question = require("../../rest-server/schema/questions");
const mockData = require("../mongoose-mock-data");

jest.mock("../../rest-server/utils/errorHandler", () => ({
  errorCatch : jest.fn(),
  errorThrow : jest.fn()
}));

jest.mock("../../rest-server/utils/lg");

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
      expect(mockKeywords).toHaveBeenCalled();
    });

    test("Posting a new question with invalid user", async () => {
      const req = mockPostQuestionRequest("new database entry", "some question test", mongoose.Types.ObjectId(), "Cpen 311");
      const res = mockResponse();

      await questionController.postQuestion(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "Could not find User", 403);
    });
  });

  describe("Get Most Recent Question Tests", () => {
    const mockGetMostRecentQuestion = (id) => {
      return {
        params : {
          userId : id
        }
      }
    };

    test("Get most recent question by existing user", async () => {
      const req = mockGetMostRecentQuestion(mockData.testUser._id);
      const res = mockResponse();

      await questionController.getMostRecentQuestion(req, res, next);
      let expected = await Question.findById(mockData.testQuestionArray[0]._id);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith(expected);
    });

    test("Get most recent question by non-existing user", async () => {
      const req = mockGetMostRecentQuestion(mongoose.Types.ObjectId());
      const res = mockResponse();

      await questionController.getMostRecentQuestion(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "Could not find any questions", 403);
    });

  });

  describe("Swiped Question Tests", () => {

    const mockSwipeRequest = (dir, qid, uid) => {
      return {
        body : {
          direction : dir,
          questionId : qid,
          userId : uid
        }
      }
    }

    test("Existing Question", async () => {
      const req = mockSwipeRequest("left", mockData.testQuestionArray[0]._id, mockData.testUser._id);
      const res = mockResponse();

      await questionController.swipedQuestion(req, res, next);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({user : mockData.testUser._id, direction : "left"});
    });

    test("Non-exisiting question", async () => {
      const req = mockSwipeRequest("left", mongoose.Types.ObjectId(), mockData.testUser._id);
      const res = mockResponse();

      await questionController.swipedQuestion(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "Question Not Found", 403);
    });
  
    test("Swiping on question user has already swiped on", async () => {
      const req = mockSwipeRequest("right", mockData.testQuestionArray[0]._id, mockData.testUser._id);
      const res = mockResponse();

      await questionController.swipedQuestion(req, res, next);
      await questionController.swipedQuestion(req, res, next);
      let expected = await Question.findById(mockData.testQuestionArray[0]._id);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({user : mockData.testUser._id, direction : "right"});
      expect(expected.swipedUsers.length).toEqual(1);
    });
    
  });

});