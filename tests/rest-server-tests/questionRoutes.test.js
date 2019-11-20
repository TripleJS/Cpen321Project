const questionController = require("../../rest-server/controller/question");
const errorHandler = require("../../rest-server/utils/errorHandler");

const {MongoClient} = require('mongodb');
 
describe("questionTests", () => {
  let db;

  beforeAll(async () => {
    try {
      db = await mongoose.connect(process.env.MONGO_URL, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useFindAndModify: false
      });
    } catch (e) {
      process.exit();
    }
  });

  beforeEach(async () => {
    await mongoose.connection.db.dropDatabase();
  });



  describe("", async () => {
  });

});