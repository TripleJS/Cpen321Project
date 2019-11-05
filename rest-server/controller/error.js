/* Main Error Controller, used for api to return error status code 
    and error message
*/ 
const errorController = (err, req, res, next) => {
    console.error(err);
    const status = err.statusCode || 500; // Passed error status code or server side error
    const errMessage = err.message;
    const errData = err.data;

    res.status(status).json({
            message: errMessage,
            data: errData
        }
    );
};

module.exports = {
    errorController
};