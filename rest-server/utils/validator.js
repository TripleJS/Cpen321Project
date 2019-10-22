const {body} = require('express-validator');
const User = require('../schema/user');

const userValidator = body('owner')
    .custom(async (value) =>
    {

        console.log(value);
        try {
            let user = await User.findById(value);

        if (!user)
            return Promise.reject('User Does Not Exist');
        } catch (err) {
            next(err);
        }
        
    });


module.exports = {
    userValidator
}