var admin = require("firebase-admin");

var serviceAccount = require("./ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://ubconnect-ec25e.firebaseio.com"
});

// var refreshToken; // Get refresh token from OAuth2 flow

// admin.initializeApp({
//   credential: admin.credential.refreshToken(refreshToken),
//   databaseURL: 'https://<DATABASE_NAME>.firebaseio.com'
// });

// Initialize the default app
// var admin = require('firebase-admin');
// var app = admin.initializeApp();