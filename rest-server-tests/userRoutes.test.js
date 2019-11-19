const userController = require("../rest-server/controller/user");
const errorHandler = require("../rest-server/utils/errorHandler");
const {MongoClient} = require('mongodb');

describe("User Route Test Suite", () => {
  let connection;
  let db;

  const errorCatchMock = jest.spyOn(errorHandler, "errorCatch");
  const errorThrowMock = jest.spyOn(errorHandler, "errorThrow");
                  
  beforeAll(async () => {
    connection = await MongoClient.connect(process.env.MONGO_URL, {
      useNewUrlParser: true,
      useUnifiedTopology: true
    });
    db = await connection.db();
  });
  
  afterAll(async () => {
    await connection.close();
  });

  describe("Testing Signup", () => {

    const users = db.collection('users');

    const mockRequest = (email, username, password) => {
      const req = {};
      req.email = jest.fn().mockReturnValue(req);
      req.username = jest.fn().mockReturnValue(req);
      req.password = jest.fn().mockReturnValue(req);

      return req;
    };

    const mockResponse = () => {
      const res = {};
      res.status = jes.fn().mockReturnValue(res);
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







