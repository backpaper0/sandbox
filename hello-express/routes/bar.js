var express = require('express');
var router = express.Router();

router.use((req, res, next) => {
  console.log("bar");
  next();
});

router.get('/', (req, res, next) => {
  res.send("bar");
});

module.exports = router;
