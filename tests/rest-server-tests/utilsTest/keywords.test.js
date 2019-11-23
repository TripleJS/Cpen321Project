const keywords = require("../../../rest-server/utils/lg");


describe("Keyword Test Suite", () => {

    test("Empty String", () => {
        const testString = "";
        expect(keywords(testString)).toEqual([""]);
    });

    test("Sentence with 1 word", () => {
        const testString = "Hello";
        expect(keywords(testString)).toEqual(["Hello"]);
    });

    test("Sentence with all keywords", () => {
        const testString = "Hello my name";
        expect(keywords(testString)).toEqual(["Hello", "my", "name"]);
    });

    test("Sentence with no keywords", () => {
        const testString = "the is to";
        expect(keywords(testString)).toEqual([""]);
    });

    test("Sentence with 5 non-keywords", () => {
        const testString = "hello to my name is jeff john";
        expect(keywords(testString)).toEqual(["hello", "my", "name", "jeff", "john"]);
    });

    test("Sentence that has all the non-keywords in our list" , () => {
        const testString = "the a an to by for in and when where what how why is are or of, 91, + =";
        expect(keywords(testString)).toEqual([""]);
    });

});

