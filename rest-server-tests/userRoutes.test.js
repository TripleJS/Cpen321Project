const userController = require("../rest-server/controller/user");
import * as errorHandler from "../rest-server/utils/errorHandler";

describe("Check Login", async () => {
    const errorCatchMock = jest.spyOn(errorHandler, "errorCatch");
    const errorThrowMock = jest.spyOn(errorHandler, "errorThrow");

    test("Logging In With Email", async () => {
        
    });

    test("Logging in With ", async() => {

    });

    test("Logging in With Email where User Exists", async () => {

    });

    test("Logging in With Facebook", async () => {

    });
});


describe("Testing Signup", async () => {
    test("Signing Up Correctly", async () => {

    });

    test("Signing Up with Email that Exists", async () => {

    });
});

describe("Testing Rate", async () => {
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

describe("Testing Report", async () => {
    test("Reporting With Correct Inputs", async () => {

    });

    test("Reporting a user that does not exist", async() => {

    });

    test("Reporting a user with a user that does not exist", async() => {

    });

    test("Reporting a user twice", async () => {

    });


});






