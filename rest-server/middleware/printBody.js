const printBody = (req, res, next) => {
    console.log(req.body);
    next();
};

module.exports = printBody;