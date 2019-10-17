var express = require('express');
var router = express.Router();

router.use((req, res, next) => {
  console.log("foo");
  next();
});

router.get('/', (req, res, next) => {
  res.send("GET foo");
});

router.post('/', (req, res, next) => {
  res.send("POST foo");
});

module.exports = router;
