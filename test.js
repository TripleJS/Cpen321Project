const testobj = [{id : 2, rating: 4}, {id : 2, rating: 5}, {id : 2, rating: 3}, {id : 2, rating: 2}];

const total = testobj.reduce((a, b) => {
    return {rating : a.rating + b.rating};
});

console.log(total);