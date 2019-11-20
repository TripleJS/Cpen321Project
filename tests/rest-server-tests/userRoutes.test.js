const userController = require("../../rest-server/controller/user");
const mongoose = require("mongoose");
const User = require("../../rest-server/schema/user");
const mockData = require("../mongoose-mock-data");

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
      
      const user = new User(
        mockData.testUser
      );

      await user.save();
      await userController.addUser(req, res, next);
      expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User already Exists", 403);
    });

  });

  describe("Check Login", () => {

      test("Logging In With Email", async () => {
          let user = new User(mockData.testUser);
          await user.save();

          const req = mockLoginSignupRequest(mockData.testUser.email, mockData.testUser.userName, mockData.testUser.password);
          const res = mockResponse();

          await userController.loginUser(req, res, next);
          expect(res.status).toHaveBeenCalledWith(201);
          expect(res.json).toHaveBeenCalledWith({userId : mockData.testUser._id, jwt : mockData.testJwt});
      });

      test("Logging in With Email where User doesn't exist", async () => {
        const req = mockLoginSignupRequest(mockData.testUser.email, mockData.testUser.userName, mockData.testUser.password);
        const res = mockResponse();

        await userController.loginUser(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "User does not exist", 404);
      });

      test("Logging in With incorrect Password", async () => {
        let user = new User(mockData.testUser);
        await user.save();

        const req = mockLoginSignupRequest(mockData.testUser.email, mockData.testUser.userName, "incorrect password");
        const res = mockResponse();

        await userController.loginUser(req, res, next);
        expect(mockErrorHandler.errorThrow).toHaveBeenCalledWith({}, "Incorrect Password", 403);
      });

      test("Logging in With Facebook", async () => {
        const user = new User(mockData.testFacebookUser);
        const res = mockResponse();
        await user.save();

        const req = {
          body : {
            fcmAccesToken : "sometoken"
          },
          user : user
        };

        await userController.oAuthLogin(req, res, next);
        expect(res.json).toHaveBeenCalledWith({userId : mockData.testFacebookUser._id, jwt : mockData.testJwt});
      });
  });

  describe("Testing Rate",  () => {

      const mockRatingRequest = (mockUserId, mockUserRatingId, mockRating) => {
        const req = {
          body : {
            userId : mockUserId,
            rating : mockRating
          }, 
          params : {
            ratingUserId : mockUserRatingId
          }
        };

        return req; 
      };

      test("Rating With Correct Inputs", async () => {
        const req = mockRatingRequest();
        const res = mockResponse(); 
        const user = new User(mockData.testUser);
        const user2 = new User(mockData.testUser2);
        await 
      });

      test("Rating a user that does not exist", async() => {

      });

      test("Rating a user with a user that does not exist", async() => {

      });


      test("Rating a user twice with same inputs", async () => {

      });

      test("Rating a user twice with different inputs", async() => {

      });

  }); 

  describe("Testing Report",  () => {
      test("Reporting With Correct Inputs", async () => {

      });

      test("Reporting a user that does not exist", async() => {

      });

      test("Reporting a user with a user that does not exist", async() => {

      });

      test("Reporting a user twice", async () => {

      });
  });

  describe("Testing GetUser", () => {
    test("Get existing User", async () => {

    });

    test("Get non-exisiting User", async () => {

    });
  });

  describe("Testing Update User", () => {
    test("Updating existing User", async () => {

    });

    test("Updating non-existing User", async () => {

    });

    test("Updating User with its current values", async () => {

    });

    test("Updating only username", async () => {

    });

    test("Updating only courses", async () => {

    });

    test("Updating only email", async () => {

    });
  });

})







