const questionController = require("../rest-server/controller/question");
const errorHandler = require("../rest-server/utils/errorHandler");

const {MongoClient} = require('mongodb');
 
describe('insert', () => {
  let connection;
  let db;
 
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

});



describe("", async () => {


});