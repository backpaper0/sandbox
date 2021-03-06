var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./dist/routes/index');
const fooRouter = require('./routes/foo');
const barRouter = require('./routes/bar');

var app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/foo', fooRouter);
app.use('/bar', barRouter);

app.use((err, req, res, next) => {
  res.status(500).json(err);
});

module.exports = app;
