const {body} = require("express-validator");
const User = require("../schema/user");
const {logger} = require("../../logger");

const userValidator = body("userId")
    .custom(async (value) => {
        logger.info("user id is :" + value);

        try {
            let user = await User.findById(userId);
            
            if (!user) {
                return Promise.reject("User Does Not Exist");
            }
            else {
                return true; 
            }

        } catch (err) {
            logger.error(err);
            return Promise.reject(err);
        }
    });


module.exports = {
    userValidator
};