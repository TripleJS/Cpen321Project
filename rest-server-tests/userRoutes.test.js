const {testFunc} = require('../test1');

test("adding", () => {
    expect(testFunc(2,3)).toEqual(5);
});


