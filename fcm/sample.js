var admin = require("firebase-admin");

var serviceAccount = require("./ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://ubconnect-ec25e.firebaseio.com"
});

// var refreshToken; // Get refresh token from OAuth2 flow

// admin.initializeApp({
//   credential: admin.credential.refreshToken(refreshToken),
//   databaseURL: "https://<DATABASE_NAME>.firebaseio.com"
// });

// Initialize the default app
// var admin = require("firebase-admin");
// var app = admin.initializeApp();

// This registration token comes from the client FCM SDKs.
// var registrationToken = "YOUR_REGISTRATION_TOKEN";

// var message = {
//   data: {
//     score: "850",
//     time: "2:45"
//   },
//   token: registrationToken
// };

// Send a message to the device corresponding to the provided
// registration token.
// admin.messaging().send(message)
//   .then((response) => {
    // Response is a message ID string.
//     console.log("Successfully sent message:", response);
//   })
//   .catch((error) => {
//     console.log("Error sending message:", error);
//   });


  // These registration tokens come from the client FCM SDKs.
// const registrationTokens = [
//     "YOUR_REGISTRATION_TOKEN_1",
//     // …
//     "YOUR_REGISTRATION_TOKEN_N",
//   ];
  
//   const message = {
//     data: {score: "850", time: "2:45"},
//     tokens: registrationTokens,
//   }
  
//   admin.messaging().sendMulticast(message)
//     .then((response) => {
//       if (response.failureCount > 0) {
//         const failedTokens = [];
//         response.responses.forEach((resp, idx) => {
//           if (!resp.success) {
//             failedTokens.push(registrationTokens[idx]);
//           }
//         });
//         console.log("List of tokens that caused failures: " + failedTokens);
//       }
//     });