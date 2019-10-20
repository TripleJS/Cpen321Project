const {body} = require('express-validator');
const User = require('../schema/user');

const userValidator = body('id')
    .isAlphanumeric()
    .withMessage('Invalid UserName')
    .custom(async (value) =>
    {
        let user = await User.findOne({userName: value});

        if (user)
            return Promise.reject('Username already exists');
    });


module.exports = {
    userValidator
}