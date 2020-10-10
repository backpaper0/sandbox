var express = require('express');
//var path = require('path');
var cookieParser = require('cookie-parser');
var morgan = require('morgan');
var cors = require('cors');
var log4js = require('log4js');
var logger = log4js.getLogger();
logger.level = 'debug';

var indexRouter = require('./routes/index');

var app = express();

app.use(morgan('dev'));

// http://expressjs.com/en/resources/middleware/cors.html
var corsConfig = {
  origin: (process.env.CORS_ORIGIN || 'https://spa.example.com:8000'),
  credentials: true,
};
logger.debug('CORS config', corsConfig);
app.use(cors(corsConfig));

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
//app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);

module.exports = app;
