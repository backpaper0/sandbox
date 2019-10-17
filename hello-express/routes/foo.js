var express = require('express');
var router = express.Router();

router.use((req, res, next) => {
  console.log("foo");
  next();
});

router.get('/', (req, res, next) => {
  res.send("foo");
});

module.exports = router;
