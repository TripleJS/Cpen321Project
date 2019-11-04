const passport = require("passport");
const GooglePlusTokenStrategy = require("passport-google-plus-token");
const FacebookTokenStrategy = require("passport-facebook-token");
const JwtStrategy = require("passport-jwt").Strategy;
const { ExtractJwt } = require("passport-jwt");
const {googleClientID, googleClientSecret, facebookClientID, facebookClientSecret} = require("../../config");
const User = require("../schema/user");
const {errorThrow} = require("../utils/errorHandler");
const {secretKey} = require("../../config");

const createNewUser = (profile, method) => {
    const id = profile.id;
    const email = profile.emails[0].value;
                
    console.log(email);
    let user; 

    if (method === "facebook") {
    user = new User({
        method: method,
            facebook: {
            id: id,
            email: email
            },
            userName: email
        });
    } else if (method === "google") {
        user = new User({
            method: method,
            google: {
            id: id,
            email: email
        },
            userName: email
        });
    }

    return user; 
}

const oAuthLogin = (method) => {
    return async(accessToken, refreshToken, profile, done) => {
        try {
            console.log("Printing Access Token: " + accessToken);

            let user; 

            if (method === "facebook") {
                user = await User.findOne({ "facebook.id": profile.id });
            }
            else if (method === "google") {
                user = await User.findOne({"google.id" : profile.id});
            }
                
            console.log(profile);
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
    console.log(payload);
    console.log("------------------------------------------");
    console.log("Passed JWT Authentication");
    try {
        // Find user specified in token
        const user = await User.findById(payload.user);

        if (!user)
            errorThrow({}, "User Does not Exist", 403);

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



// const oAuthLoginFB = async(accessToken, refreshToken, profile, done) => {
//     try {
//         console.log(accessToken);
//         const user = await User.findOne({ "facebook.id": profile.id });

//         if (!user)
//         {
//             const id = profile.id;
//             const email = profile.emails[0].value;

//             const user = new User({
//                     method: "facebook",
//                     facebook: {
//                         id: id,
//                         email: email
//                     },
//                     userName: email
//                 }
//             );

//             let result = await user.save();
//             return done(null, result)
//         }
            
//         console.log("here");
//         done(null, user);
//     } catch (error) {
//         done(error, false);
//     }
// };


// const oAuthLoginGoogle = async(accessToken, refreshToken, profile, done) => {
//     try {
//         console.log(accessToken);
//         const user = await User.findOne({ "google.id": profile.id });

//         if (!user)
//         {
//             const id = profile.id;
//             const email = profile.emails[0].value;

//             const user = new User({
//                     method: "google",
//                     google: {
//                         id: id,
//                         email: email
//                     },
//                     userName: email
//                 }
//             );

//             let result = await user.save();
//             return done(null, result)
//         }
            
//         console.log("here");
//         done(null, googleProfile);
//     } catch (error) {
//         done(error, false);
//     }
// };

