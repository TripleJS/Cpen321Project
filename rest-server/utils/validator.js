const {body} = require('express-validator');
const User = require('../schema/user');

const userValidator = body('userID')
    .custom(async (value) =>
    {
        let user = await User.findOne({userID: value});

        if (!user)
            return Promise.reject('User Does Not Exist');
    });


module.exports = {
    userValidator
}