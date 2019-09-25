const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema(
    {
        method : 
        {
            type : String,
            enum : ['local', 'google', 'facebook'],
            required : true
        },
        local : 
        {
            type:
            {
                passwordHash: {
                    type: String,
                    required: true,
                    select: false
                }, 
                resetToken : String,
                resetExpirationDate: Date,
                email: 
                {
                    type: String,
                    required: true
                },
            },
            required: false
        },
        google: {
            id : {
                type: Number,
            },
            email: {
                type: String,
                lowercase: true
            }
        }, 
        facebook: {
            id : {
                type: String,
            },
            email: {
                type: String,
                lowercase: true
            }
        },
        userName : 
        {
            type: String,
            required: true
        },
        friendsList :
        {
            type : [Schema.Types.ObjectId],
            required: true,
            default : [],
            ref : 'User'
        },
        roomsList : 
        {
            type : [Schema.Types.ObjectId],
            required: true,
            default: [],
            ref: 'Rooms'
        },
        icon : String
    }
);


module.exports = mongoose.model('User', userSchema);