const admin = require("firebase-admin");
const serviceAccount = require("../../fcm/ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://ubconnect-ec25e.firebaseio.com"
  });