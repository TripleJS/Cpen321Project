const testobj = [{id : 2, rating: 4}];

const total = testobj.reduce((a, b) => {
    return {rating : a.rating + b.rating};
}, {rating : 0});


const testFunc = (a, b) => {
    return a + b;
};

module.exports = {
    testFunc
}