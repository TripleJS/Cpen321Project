const CognitiveServicesCredentials = require("@azure/ms-rest-js");
const TextAnalyticsAPIClient = require("@azure/cognitiveservices-textanalytics");

const {subscriptionKey, endpoint} = require("../../../config");

const creds = new CognitiveServicesCredentials.ApiKeyCredentials({ inHeader: { "Ocp-Apim-Subscription-Key": subscriptionKey } });
const client = new TextAnalyticsAPIClient.TextAnalyticsClient(creds, endpoint);

const getKeywords = (inputDocuments) => {
    const operation = client.keyPhrases({multiLanguageBatchInput: inputDocuments});

    return operation.then((result) => {
        console.log("here " + result.documents[0].keyPhrases);

        return Promise.resolve(result.documents[0].keyPhrases);
    })
    .catch((err) => {
        throw err;
    });
}

module.exports = getKeywords;



