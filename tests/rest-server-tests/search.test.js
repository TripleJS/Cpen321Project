const search = require("../../rest-server/controller/search");
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
  });