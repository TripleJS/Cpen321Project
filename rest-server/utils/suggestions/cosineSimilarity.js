
const getCosineSimilarity = (v1, v2) => {

    let sumxx = 0; let sumyy = 0; let sumxy = 0;
    if(v1.length < v2.length) {
        for (i of v1) {
            let x = v1[i]; 
            let y = v2[i];
            sumxx += x*x;
            sumyy += y*y;
            sumxy += x*y;
        }
            
    } else {
        for(i of v2) {
            let x = v1[i]; let y = v2[i]
            sumxx += x*x
            sumyy += y*y
            sumxy += x*y
        }

    }
    return sumxy/Math.sqrt(sumxx*sumyy)
    
}
const getBagOfQuestions = (questionList, question) => {
    const bagOfQuestions = [];
    
    
    for (i = 0; i < QuestionList.size; i++) {
    
        
        const cosineSimilarity = getCosineSimilarity (question, questionList(i));
        if (cosineSimilarity > 0.4) {
            bagOfQuestions.add(questionList(i))
        }
        if (bagOfQuestions > 4) {
            return bagOfQuestions
        }
        
    }

    
}


