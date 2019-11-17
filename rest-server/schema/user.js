const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const userSchema = new Schema({
        method : {
            type : String,
            enum : ["local", "google", "facebook"],
            required : true
        },
        email: {
            type : String, 
            required : true
        },
        local : {
            passwordHash: {
                type: String,
                select: false
            }, 
            resetToken : String,
            resetExpirationDate: Date,
            email: String
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
        }, userName : {
            type: String
        }, name: {
            type: String
        },
        icon : String,
        rating : {
            type: Number,
            required: true,
            default: 0
        },
        banned : {
            type: Boolean
        }, 
        courses: {
            type : [String],
            default: [],
            required : true
        },
        answers : {
            type: [mongoose.Schema.Types.ObjectId], 
            ref: "Answers",
            default : [],
            required: true
        },
        groups : {
            type : [mongoose.Schema.Types.ObjectId],
            ref : "Groups",
            required : true,
            default : []
        },
        fcmAccessToken : {
            type : String
        },
        reported : {
            type : [mongoose.Schema.Types.ObjectId]
        }
    }
);


module.exports = mongoose.model("User", userSchema);