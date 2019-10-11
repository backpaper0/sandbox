/*

curl -b ck -c ck -v localhost:3000/auth -d username=demo -d password=secret

*/
const express = require('express');
const router = express.Router();
const passport = require("passport");

router.post('/', passport.authenticate('local'), async (req, res, next) => {

	res.send('Hello, passport!');
});

module.exports = router;
