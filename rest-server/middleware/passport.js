const passport = require("passport");
const GooglePlusTokenStrategy = require("passport-google-plus-token");
const FacebookTokenStrategy = require("passport-facebook-token");
const JwtStrategy = require("passport-jwt").Strategy;
const { ExtractJwt } = require("passport-jwt");
const {googleClientID, googleClientSecret, facebookClientID, facebookClientSecret} = require("../../config");
const User = require("../schema/user");
const {errorThrow} = require("../utils/errorHandler");
const {secretKey} = require("../../config");
const {logger} = require('../../logger');

const createNewUser = (profile, loginMethod) => {
    const newId = profile.id;
    const userEmail = profile.emails[0].value;
                
    logger.info(userEmail);
    let user; 

    if (loginMethod === "facebook") {
    user = new User({
        method: loginMethod,
            facebook: {
            id: newId,
            email: userEmail
            },
            userName: userEmail
        });
    } else if (loginMethod === "google") {
        user = new User({
            method: loginMethod,
            google: {
            id: newId,
            email: userEmail
        },
            userName: userEmail
        });
    }

    return user; 
};

const oAuthLogin = (method) => {
    return async(accessToken, refreshToken, profile, done) => {
        try {
            logger.info("Printing Access Token: " + accessToken);

            let user; 

            if (method === "facebook") {
                user = await User.findOne({"facebook.id": profile.id});
            } else {
                user = await User.findOne({"google.id" : profile.id});
            }
                
            logger.info(profile);
            
            if (!user) {
                user = createNewUser(profile, method);
            }
                
            done(null, user);
        } catch (error) {
            done(error, false);
        }
    };
};

// JWT strategy 
passport.use(new JwtStrategy({
    jwtFromRequest: ExtractJwt.fromHeader("authorization"),
    secretOrKey: secretKey
}, async(payload, done) => {

    logger.info(payload);
    logger.info("------------------------------------------");
    logger.info("Passed JWT Authentication");
    try {
        // Find user specified in token
        const user = await User.findById(payload.user);

        if (!user) {
            errorThrow({}, "User Does not Exist", 403);
        }
            
        done(null, user);
    } catch (error) {
        done(error, false);
    }
}));

passport.use("Google-Login", new GooglePlusTokenStrategy({
    clientID: googleClientID,
    clientSecret: googleClientSecret
}, oAuthLogin("google")));

passport.use("Facebook-Login", new FacebookTokenStrategy({
    clientID: facebookClientID, 
    clientSecret: facebookClientSecret
}, oAuthLogin("facebook")));


