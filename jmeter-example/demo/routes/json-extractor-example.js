var express = require('express');
var router = express.Router();

router.get('/', function(req, res, next) {
  res.json({
    foo: {
      bar: {
        baz: 'asdfghjkl',
      }
    }
  });
});

router.post('/', function(req, res, next) {
  if (req.body.foobarbaz !== 'asdfghjkl') {
    res.sendStatus(400);
    return;
  }
  res.json({
    status: 'Success',
  });
});

module.exports = router;
