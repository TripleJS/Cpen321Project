const {logger} = require('../../logger');

const printBody = (req, res, next) => {
    logger.info(req.body);
    next();
};

module.exports = printBody;