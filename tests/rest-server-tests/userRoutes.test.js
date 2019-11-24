const userController = require("../../rest-server/controller/user");
const mongoose = require("mongoose");
const User = require("../../rest-server/schema/user");
const mockData = require("../mongoose-mock-data");
const jwt = require("jsonwebtoken");
const {secretKey} = require("../../config");

jest.mock("../../rest-server/utils/errorHandler", () => ({
  errorCatch : jest.fn(),
  errorThrow : jest.fn()
}));

const mockErrorHandler = require("../../rest-server/utils/errorHandler");

describe("User Route Test Suite", () => {
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
  })

  afterEach(async () => {
    await mongoose.connection.dropDatabase();
  });

  const mockLoginSignupRequest = (email, username, password) => {
    const req = {
      body : {
        email : email,
        userName : username,
        password : password
      }
    };

    return req;
  };

  const mockResponse = () => {
    const res = {};
    res.status = jest.fn().mockReturnValue(res);
    res.json = jest.fn().mockReturnValue(res);
    return res;
  };

  const next = () => {};

  describe("Testing Signup", () => {
    
    test("Signing Up Correctly", async () => {
      const req = mockLoginSignupRequest("someemail@email.com", "someusername", "password");
      const res = mockResponse();
  
      await userController.addUser(req, res, next);
      expect(res.status).toHaveBeenCalledWith(201);
    });

    test("Signing Up with Email that Exists", async () => {
      const req = mockLoginSignupRequest("testemail@email.com", "someusername", "password");
      const res = mockResponse();

      await userController.addUser(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User already Exists", 403);
    });

  });

  describe("Check Login", () => {

      test("Logging In With Email", async () => {
          const req = mockLoginSignupRequest(mockData.testUser.email, mockData.testUser.userName, mockData.testUser.password);
          const res = mockResponse();

          const testJwt = jwt.sign({
            user: mockData.testUser._id
          },
            secretKey, 
            { expiresIn: "24h" },
          );
      
          await userController.loginUser(req, res, next);
          expect(res.status).toHaveBeenCalledWith(201);
          expect(res.json).toHaveBeenCalledWith({jwt : testJwt, _id : mockData.testUser._id});
      });

      test("Logging in With Email where User doesn't exist", async () => {
        const req = mockLoginSignupRequest("somenotexistentemail@email.com", mockData.testUser.userName, mockData.testUser.password);
        const res = mockResponse();

        await userController.loginUser(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User does not exist", 404);
      });

      test("Logging in With incorrect Password", async () => {
        const req = mockLoginSignupRequest(mockData.testUser.email, mockData.testUser.userName, "incorrect password");
        const res = mockResponse();

        await userController.loginUser(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "Incorrect Password", 403);
      });

      test("Logging in With Facebook", async () => {
        let user = await User.findById(mockData.testFacebookUser._id);
        const res = mockResponse();
        const req = {
          body : {
            fcmAccesToken : "sometoken"
          },
          user : user
        };

        const testJwt = jwt.sign({
          user: mockData.testFacebookUser._id
        },
          secretKey, 
          { expiresIn: "24h" },
        );
        
        await userController.oAuthLogin(req, res, next);
        expect(res.json).toHaveBeenCalledWith({jwt : testJwt, _id : mockData.testFacebookUser._id});
      });
  });

  describe("Testing Rate",  () => {

      const mockRatingRequest = (mockUserId, mockUserRatingId, mockRating) => {
        const req = {
          body : {
            _id : mockUserId,
            rating : mockRating
          }, 
          params : {
            ratingUserId : mockUserRatingId
          }
        };

        return req; 
      };

      test("Rating With Correct Inputs", async () => {
        const req = mockRatingRequest(mockData.testUser._id, mockData.testUser2._id, 5);
        const res = mockResponse(); 

        await userController.rate(req, res, next);
        expect(res.status).toHaveBeenCalledWith(200);
      });

      test("Rating a user that does not exist", async() => {
        const req = mockRatingRequest(mongoose.Types.ObjectId(), mockData.testUser2._id, 5);
        const res = mockResponse(); 
        await userController.rate(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User doesn't Exist", 404);
      });

      test("Rating a user with a user that does not exist", async() => {
        const req = mockRatingRequest(mockData.testUser2._id, mongoose.Types.ObjectId(), 5);
        const res = mockResponse(); 

        await userController.rate(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User doesn't Exist", 404);
      });


      test("Rating a user twice with same inputs", async () => {
        const req = mockRatingRequest(mockData.testUser._id, mockData.testUser2._id, 5);
        const res = mockResponse(); 

        await userController.rate(req, res, next);
        await userController.rate(req, res, next);
        let result = await User.findById(mockData.testUser._id);
        expect(result.rating).toEqual(5);
        expect(result.usersWhoRated.length).toEqual(1);
      });

      test("Rating a user twice with different inputs", async() => {
        const req = mockRatingRequest(mockData.testUser._id, mockData.testUser2._id, 5);
        const req2 = mockRatingRequest(mockData.testUser._id, mockData.testUser2._id, 3);
        const res = mockResponse(); 

        await userController.rate(req, res, next);
        await userController.rate(req2, res, next);
        let result = await User.findById(mockData.testUser._id);
        console.log(result);
        expect(result.rating).toEqual(3);
        expect(result.usersWhoRated.length).toEqual(1);
      });

      test("Rating a user with 2 different users", async () => {
        const req = mockRatingRequest(mockData.testUser._id, mockData.testUser2._id, 5);
        const req2 = mockRatingRequest(mockData.testUser._id, mockData.testUser3._id, 3);
        const res = mockResponse(); 
        await userController.rate(req, res, next);
        await userController.rate(req2, res, next);
        let result = await User.findById(mockData.testUser._id);

        expect(result.rating).toEqual(4);
        expect(result.usersWhoRated.length).toEqual(2);
      });

  }); 

  describe("Testing Report",  () => {

      const mockReportRequest = (mockUserId, mockUserReportingId) => {
        const req = {
          body : {
            _id : mockUserId,
          }, 
          params : {
            reportingUserId : mockUserReportingId
          }
        }
        
        return req;
      };

      test("Reporting With Correct Inputs", async () => {
        const req = mockReportRequest(mockData.testUser._id, mockData.testUser2._id);
        const res = mockResponse(); 
        await userController.report(req, res, next);
        expect(res.status).toHaveBeenCalledWith(200);
      });

      test("Reporting a user that does not exist", async() => {
        const req = mockReportRequest(mongoose.Types.ObjectId(), mockData.testUser2._id);
        const res = mockResponse(); 
        await userController.report(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User doesn't Exist", 404);
      });

      test("Reporting a user with a user that does not exist", async() => {
        const req = mockReportRequest(mockData.testUser2._id, mongoose.Types.ObjectId);
        const res = mockResponse(); 
        await userController.report(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User doesn't Exist", 404);
      });

      test("Reporting a user twice with same user", async () => {
        const req = mockReportRequest(mockData.testUser2._id, mockData.testUser._id);
        const req2 = mockReportRequest(mockData.testUser2._id, mockData.testUser._id);
        const res = mockResponse(); 
        await userController.report(req, res, next);
        await userController.report(req2, res, next);
        let result = await User.findById(mockData.testUser2._id);
        expect(result.usersWhoReported.length).toEqual(1);
      });

      test("Reporting a user multiple times with different users", async () => {
        const req = mockReportRequest(mockData.testUser2._id, mockData.testUser._id);
        const req2 = mockReportRequest(mockData.testUser2._id, mockData.testUser3._id);
        const res = mockResponse(); 
        await userController.report(req, res, next);
        await userController.report(req2, res, next);
        let result = await User.findById(mockData.testUser2._id);
        expect(result.usersWhoReported.length).toEqual(2);
      });
  });

  describe("Testing GetUser", () => {
    const mockGetUserRequest = (id) => {
      return {
        params : {
          userId: id
        }
      }
    };

    test("Get existing User", async () => {
      const req = mockGetUserRequest(mockData.testUser._id);
      const res = mockResponse();
      await userController.getUser(req, res, next);
      expect(res.status).toHaveBeenCalledWith(200);
    });

    test("Get non-exisiting User", async () => {
      const req = mockGetUserRequest(mongoose.Types.ObjectId());
      const res = mockResponse();
      await userController.getUser(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User Does Not Exist", 403);      
    });
  });

  describe("Testing Update User", () => {
    const mockUpdateUserRequest = (mockUserName, mockCourses, mockEmail, id) => {
      return {
        body : {
          userName : mockUserName,
          courses : mockCourses,
          email : mockEmail
        },
        params : {
          userId : id
        }
      }
    };

    test("Updating existing User", async () => {
      const req = mockUpdateUserRequest("New Username", ["Cpen 321", "Cpen 331"], "newemail@email.com", mockData.testUser._id);
      const res = mockResponse();

      await userController.updateUser(req, res, next);
      let user = await User.findById(mockData.testUser._id);
      const updatedUser = user.toObject();
      updatedUser.questions = [];
      expect(res.status).toHaveBeenCalledWith(200);
      // expect(res.json).toHaveBeenCalledWith(updatedUser);
    });

    test("Updating non-existing User", async () => {
      const req = mockUpdateUserRequest("New Username", ["Cpen 321", "Cpen 331"], "newemail@email.com", mongoose.Types.ObjectId());
      const res = mockResponse();

      await userController.updateUser(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User Does Not Exist", 403);
    });

    test("Updating User with its current values", async () => {
      const req = mockUpdateUserRequest(mockData.testUser.userName, [], mockData.testUser.email, mockData.testUser._id);
      const res = mockResponse();

      await userController.updateUser(req, res, next);
      user = await User.findById(mockData.testUser._id);
      const updatedUser = user.toObject();
      updatedUser.questions = [];
      expect(res.status).toHaveBeenCalledWith(200);
      // expect(res.json).toHaveBeenCalledWith(updatedUser);
    });

    test("Updating only username", async () => {
      const req = mockUpdateUserRequest("newusername", [], mockData.testUser.email, mockData.testUser._id);
      const res = mockResponse();
      await userController.updateUser(req, res, next);

      user = await User.findById(mockData.testUser._id);
      const updatedUser = user.toObject();
      updatedUser.questions = [];
      expect(res.status).toHaveBeenCalledWith(200);
      // expect(res.json).toHaveBeenCalledWith(updatedUser);
    });

    test("Updating only courses", async () => {
      const req = mockUpdateUserRequest(mockData.testUser.userName, ["Cpen 321"], mockData.testUser.email, mockData.testUser._id);
      const res = mockResponse();
      await userController.updateUser(req, res, next);

      user = await User.findById(mockData.testUser._id);
      const updatedUser = user.toObject();
      updatedUser.questions = [];
      expect(res.status).toHaveBeenCalledWith(200);
      // expect(res.json).toHaveBeenCalledWith(updatedUser);
    });

    test("Updating only email", async () => {
      const req = mockUpdateUserRequest(mockData.testUser.userName, [], "newemail@email.com", mockData.testUser._id);
      const res = mockResponse();
      await userController.updateUser(req, res, next);

      let user = await User.findById(mockData.testUser._id);
      const updatedUser = user.toObject();
      updatedUser.questions = [];
      expect(res.status).toHaveBeenCalledWith(200);
      // expect(res.json).toHaveBeenCalledWith(updatedUser);
    });
  });

})







