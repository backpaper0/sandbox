var express = require('express');
var router = express.Router();
var serialize = require('cookie').serialize;

router.get('/apple', function(req, res, next) {
  res.json({ value: 'Apple' });
});

router.get('/pie', function(req, res, next) {
  res.json({ value: 'Pie' });
});

router.get('/count', function(req, res, next) {
  const count = Number.parseInt(req.cookies.count) || 0;
  res.json({ value: count });
});

router.post('/count', function(req, res, next) {
  const count = Number.parseInt(req.cookies.count) || 0;
  const newCount = count + 1;
  // https://expressjs.com/en/4x/api.html#res.cookie
//  res.cookie('count', `${newCount}`, {
//    httpOnly: true,
//    secure: true,
//    sameSite: 'none',
//  });
  res.append('Set-Cookie', serialize('count', `${newCount}`, {
    httpOnly: true,
    secure: true,
    sameSite: 'none',
  }));
  res.json({ value: newCount });
});

module.exports = router;
