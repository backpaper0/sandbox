var express = require('express');
var router = express.Router();

const authn = require("../authn");

/* GET users listing. */
router.get('/', authn, function(req, res, next) {
  res.send('respond with a resource');
});

module.exports = router;
