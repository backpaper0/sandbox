var express = require('express');
var router = express.Router();

router.post('/', function(req, res, next) {
  const { a } = req.body;
  if (a === "1") {
    throw "1";
  } else if (a === "2") {
    next("2");
  } else if (a === "3") {
    setTimeout(() => next("3"), 100);
  } else {
    res.send("Hello, world!");
  }
});

module.exports = router;
