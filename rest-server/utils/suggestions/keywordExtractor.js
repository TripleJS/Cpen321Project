
const getKeywords = async (string) => {

    try {
        const response = await fetch('westus.api.cognitive.microsoft.com//text/analytics/v2.1/keyPhrases?showStats', {
            method : 'POST',
            body : { documents : [{
                language: "en",
                id: "1",
                text: string
                }]  
            }
        });

        const responseJson = response.json();
        console.log(responseJson);   
             
    } catch (error) {
        Promise.reject(error);
    }
}