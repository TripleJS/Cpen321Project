const CognitiveServicesCredentials = require("@azure/ms-rest-js");
const TextAnalyticsAPIClient = require("@azure/cognitiveservices-textanalytics");

const {subscription_key, endpoint} = require('../../../config');

const creds = new CognitiveServicesCredentials.ApiKeyCredentials({ inHeader: { 'Ocp-Apim-Subscription-Key': subscription_key } });
const client = new TextAnalyticsAPIClient.TextAnalyticsClient(creds, endpoint);

const inputDocuments = {documents:[
    {language:"en", id:"1", text: "Let x(t) be the distance at time t, in km, from boat A to the original position of boat B (i.e. to the position of boat B at noon). And let y(t) be the distance at time t, in km, of boat B from its original position. And let z(t) be the distance between the two boats at time t."}
]};

const getKeywords = (inputDocuments) => {
    const operation = client.keyPhrases({multiLanguageBatchInput: inputDocuments});

    return operation.then(result => {
        console.log('here ' + result.documents[0].keyPhrases);

        return Promise.resolve(result.documents[0].keyPhrases);
    })
    .catch(err => {
        throw err;
    });
}

module.exports = getKeywords;



