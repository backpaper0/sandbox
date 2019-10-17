var express = require('express');
var router = express.Router();

router.use((req, res, next) => {
  console.log("foo");
  next();
});

router.route("/")
  .get((req, res, next) => {
    res.send("GET foo");
  })
  .post( (req, res, next) => {
    res.send("POST foo");
  });

module.exports = router;
