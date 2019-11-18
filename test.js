const testobj = [{id : 2, rating: 4}];

const total = testobj.reduce((a, b) => {
    return {rating : a.rating + b.rating};
}, {rating : 0});

console.log(total);