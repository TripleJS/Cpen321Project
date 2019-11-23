const winston = require("winston");
const logger = winston.createLogger({
    format: winston.format.json(),
    transports : [
        new winston.transports.File({filename : "error.log", level : "error"}),
        new winston.transports.File({filename: "combined.log"})    ]
});

logger.add(new winston.transports.Console({
    format: winston.format.simple()
}));

const loggerLevels =  {
    error: 0,
    warn: 1,
    info: 2,
    verbose: 3,
    debug: 4,
    silly: 5
};

module.exports = {
    loggerLevels,
    logger
};
