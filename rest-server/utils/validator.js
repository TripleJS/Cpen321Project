const {body} = require("express-validator");
const User = require("../schema/user");

const userValidator = body("owner")
    .custom(async (value) =>
    {
        console.log("user id is :" + value);

        try {
            let user = await User.findById(value);
            
            if (!user) {
                return Promise.reject("User Does Not Exist");
            }
            else {
                return true; 
            }

        } catch (err) {
            console.error(err);
            return Promise.reject(err);
        }
        
    });


module.exports = {
    userValidator
};