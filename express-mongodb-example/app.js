const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const tasksRouter = require('./routes/tasks');
const sessionsRouter = require('./routes/sessions');

const session = require('express-session')

const mongoose = require('mongoose');

mongoose.connect('mongodb://localhost:27017/example', { useNewUrlParser: true, useUnifiedTopology: true });
mongoose.set('debug', true);

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(session({ secret: "keyboard cat" }));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/tasks', tasksRouter);
app.use('/sessions', sessionsRouter);

module.exports = app;
