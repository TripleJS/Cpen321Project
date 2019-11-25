

const normalizeVector = (vector, length) => {
    const newVector = vector;
    let i;
    for (i = 0; i < length; i++) {
        newVector.push(0);
    }

    return newVector; 
};

const computeCosineSimilarity = (v1, v2) => {
    let sumxx = 0; let sumyy = 0; let sumxy = 0;
    let i;
    for (i = 0; i < v1.length; i++) {
        let x = v1[i]; 
        let y = v2[i];
        sumxx += x*x;
        sumyy += y*y;
        sumxy += x*y;
    }

    return sumxy/Math.sqrt(sumxx*sumyy);
};

const getCosineSimilarity = (v1, v2) => {

    let normalizedVector; 

    if (v1.length === 0 || v2.length === 0) {
        return 0;
    }

    if (v1.length < v2.length) {
        normalizedVector = normalizeVector(v1, v2.length - v1.length);
        return computeCosineSimilarity(v2, normalizedVector);
    } else {
        normalizedVector = normalizeVector(v2, v1.length - v2.length);
        return computeCosineSimilarity(v1, normalizedVector);
    }
};

module.exports = getCosineSimilarity;


