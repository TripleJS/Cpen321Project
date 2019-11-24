const {isEmpty} = require("lodash");

/**
 * Used in Catch block to move to error handling middleware.
 * @param {Error} err Error object
 * @param {cb} next next function to go to error handling middleware
 */
const errorCatch = (err, next) =>
{
    if(!err.statusCode) {
        err.statusCode = 500;
    }

    next(err);
};

/**  
* Throw an error with given message and status code
* @param {object} errors Error Object with errors  
* @param {String} message error message to be sent
* @param {Number} statusCode HTTP error status code
*/
const errorThrow = (errors, message, statusCode) => {
    const error = new Error(message);
    error.statusCode = statusCode;
    
    if (isEmpty(errors)) {
        error.data = errors;
    }
     
    throw error;
};

const exportFunctions = {
    errorThrow,
    errorCatch
};

module.exports = exportFunctions;