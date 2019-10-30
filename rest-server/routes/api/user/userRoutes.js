const router = require('express').Router();
const passport = require('passport');
const passportConfig = require('../../../middleware/passport');
const userController = require('../../../controller/user');

router.post('/user/add-user', userController.addUser);
router.post('/user/oauth/google', passport.authenticate('Google-Login', {session: false}), userController.oAuthLogin);
router.post('/user/oauth/facebook', passport.authenticate('Facebook-Login', {session: false}), userController.oAuthLogin);
router.get('/user/getuser/:userId', passport.authenticate('jwt', {session: false}), userController.getUser);


module.exports = router;