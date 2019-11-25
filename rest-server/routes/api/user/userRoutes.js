const router = require("express").Router();
const passport = require("passport");
const passportConfig = require("../../../middleware/passport");
const userController = require("../../../controller/user");

router.post("/user/signup", userController.addUser);
router.post("/user/login", userController.loginUser);
router.post("/user/oauth/google", passport.authenticate("Google-Login", {session: false}), userController.oAuthLogin);
router.post("/user/oauth/facebook", passport.authenticate("Facebook-Login", {session: false}), userController.oAuthLogin);
router.get("/user/getuser/:userId", passport.authenticate("jwt", {session: false}), userController.getUser);
router.post("/user/update-user/:userId", userController.updateUser); 
router.post("/user/rate/:ratingUserId", userController.rate);
router.post("/user/report/:reportingUserId", userController.report);


module.exports = router;