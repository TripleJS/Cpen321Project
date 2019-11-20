const questionController = require("../../rest-server/controller/question");
const errorHandler = require("../../rest-server/utils/errorHandler");

const {MongoClient} = require('mongodb');
 
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


  // Test Suites: 

  describe("Get Question Tests", () => {
    test("Get Existing Question", async () => {


    });

    test ("Get non-exisiting Question", async () => {

    });

  });

  describe("Post Question Tests", () => {
    test("", async () => {

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

    test("Exisiting Question", async () => {

    });

    test("Non-existing user", async () => {

    });
    
  });
});