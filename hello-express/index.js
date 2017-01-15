const express = require('express');
const app = express();

const logger = (req, res, next) => {
  console.log('hoge');
  next();
};

app.get('/hello', (req, res) => {
  res.send('hello world')
});

app.use(logger);

app.get('/foobar', function(req, res) {
  res.send('foobar')
});

app.listen(3000);
