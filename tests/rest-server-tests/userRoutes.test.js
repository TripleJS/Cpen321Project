const userController = require("../../rest-server/controller/user");
const errorHandler = require("../../rest-server/utils/errorHandler");
const mongoose = require("mongoose");
const User = require("../../rest-server/schema/user");
const mockData = require("../mongoose-mock-data");

describe("User Route Test Suite", () => {
  let db;

  const errorCatchMock = jest.fn(errorHandler.errorCatch);
  const errorThrowMock = jest.fn(errorHandler.errorThrow);
  const signJwtandSignInMock = jest.fn(userController.signTokenAndSignIn);
                  
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

  describe("Testing Signup", () => {
    const mockRequest = (email, username, password) => {
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
      res.user = jest.fn().mockReturnValue(res);
      res.jwt = jest.fn().mockReturnValue(res);
      return res;
    };

    const next = () => {

    };

    test("Signing Up Correctly", async () => {
      const req = mockRequest("someemail@email.com", "someusername", "password");
      const res = mockResponse();
  
      await userController.addUser(req, res, next);
      expect(res.status).toHaveBeenCalledWith(201);
    });

    test("Signing Up with Email that Exists", async () => {
      const req = mockRequest("testemail@email.com", "someusername", "password");
      const res = mockResponse();
      
      const user = new User(
        mockData.testUser
      );

      let result = await user.save();
      console.log(result);
      await userController.addUser(req, res, next);
      // expect(errorThrowMock).toHaveBeenCalledWith({}, "User already Exists", 403);
      expect(signJwtandSignInMock).toHaveBeenCalledTimes(1);
    });

  });

  describe("Check Login", () => {
      const mockResponse = () => {
        const res = {};
        res.status = jest.fn().mockReturnValue(res);
        res.json = jest.fn().mockReturnValue(res);
        return res;
      };

      const mockRequest = () => {
        const res = {};
        res.user = jest.fn().mockReturnValue(res);
        res.jwt = jest.fn().mockReturnValue(res);
        return res;
      };

      test("Logging In With Email", async () => {
          
      });

      test("Logging in With ", async() => {

      });

      test("Logging in With Email where User Exists", async () => {
      });

      test("Logging in With Facebook", async () => {

      });
  });

  describe("Testing Rate",  () => {
      test("Rating With Correct Inputs", async () => {

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
})







