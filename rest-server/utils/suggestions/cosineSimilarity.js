const SIMILARITY_THRESHOLD = 0.4;
const MINIMUM_RETURNED_QUESTIONS = 4;

const questionSimilarity

const normalizeVector = (vector, length) => {
    const newVector = vector;
    for (let i = 0; i < length; i++) {
        newVector.push(0);
    }

    return newVector; 
};

const computeCosineSimilarity = (v1, v2) => {
    let sumxx = 0; let sumyy = 0; let sumxy = 0;

    for (let i = 0; i < length; i++) {
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

    if (v1.length < v2.length) {
        normalizedVector = normalizeVector(v1, v2.length - v1.length);
        return computeCosineSimilarity(v2, normalizedVector);
    } else {
        normalizedVector = normalizeVector(v2, v1.length - v2.length);
        return computeCosineSimilarity(v1, normalizedVector);
    }
};


const getBagOfQuestions = (questionKeywords, question) => {
    const bagOfQuestions = [];

    for (let i = 0; i < questionKeywords.size; i++) {
    
        const cosineSimilarity = getCosineSimilarity(question, questionKeywords[i]);
        if (cosineSimilarity > MINIMUM_RETURNED_QUESTIONS) {
            bagOfQuestions.push(questionKeywords[i]);
        }

        if (bagOfQuestions > SIMILARITY_THRESHOLD) {
            return bagOfQuestions;
        }
    }

    return bagOfQuestions;
};

module.exports = getBagOfQuestions;
