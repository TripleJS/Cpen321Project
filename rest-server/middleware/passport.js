const passport = require('passport');
const GooglePlusTokenStrategy = require('passport-google-plus-token');
const FacebookTokenStrategy = require('passport-facebook-token');
const {googleClientID, googleClientSecret, facebookClientID, facebookClientSecret} = require('../../config');
const User = require('../schema/user');

const oAuthLogin = (method) => {
    return async(accessToken, refreshToken, profile, done) => {
        try {
            console.log('Printing Access Token: ' + accessToken);

            let user; 

            if (method == 'facebook') 
                user = await User.findOne({ "facebook.id": profile.id });
            else if (method == 'google')
                user = await User.findOne({"google.id" : profile.id});
            
            if (!user) {
                const id = profile.id;
                const email = profile.emails[0].value;
                
                console.log(email);
                let newUser;

                if (method == 'facebook') {
                    newUser = new User({
                        method: method,
                        facebook: {
                            id: id,
                            email: email
                        },
                        userName: email
                    }
                 );
                } else if (method == 'google') {
                    newUser = new User({
                        method: method,
                        google: {
                            id: id,
                            email: email
                        },
                        userName: email
                    }
                    );
                }
            
                let result = await newUser.save();
                return done(null, result)
            }
                
            done(null, user);
        } catch (error) {
            done(error, false);
        }
    }
};

passport.use('Google-Login', new GooglePlusTokenStrategy({
    clientID: googleClientID,
    clientSecret: googleClientSecret
}, oAuthLogin('google')));

passport.use('Facebook-Login', new FacebookTokenStrategy({
    clientID: facebookClientID, 
    clientSecret: facebookClientSecret
}, oAuthLogin('facebook')));



// const oAuthLoginFB = async(accessToken, refreshToken, profile, done) => {
//     try {
//         console.log(accessToken);
//         const user = await User.findOne({ "facebook.id": profile.id });

//         if (!user)
//         {
//             const id = profile.id;
//             const email = profile.emails[0].value;

//             const user = new User({
//                     method: 'facebook',
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
            
//         console.log('here');
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
//                     method: 'google',
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
            
//         console.log('here');
//         done(null, googleProfile);
//     } catch (error) {
//         done(error, false);
//     }
// };

