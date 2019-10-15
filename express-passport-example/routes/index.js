var express = require('express');
var router = express.Router();

const authn = require("../authn");

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post("/login", authn.local, (req, res, next) => {
	res.send({ token: "randomtoken" });
});

module.exports = router;
