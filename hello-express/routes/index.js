var express = require('express');
var router = express.Router();

router.post('/', function(req, res, next) {
  const { a } = req.body;
  if (a === "throw") {
    throw "thrown";
  } else if (a === "error-async") {
    setTimeout(() => next("error-async"), 100);
  } else {
    res.send("Hello, world!");
  }
});

module.exports = router;
