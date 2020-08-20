var express = require('express');
var router = express.Router();

router.get('/', function(req, res, next) {
  res.send('Hello, GET request!');
});

router.post('/', function(req, res, next) {
  res.send('Hello, POST request!');
});

for (let status of [400, 401, 403, 404, 500, 503]) {
  router.get(`/${status}`, function(req, res, next) {
    res.status(status).end();
  });
}

module.exports = router;
