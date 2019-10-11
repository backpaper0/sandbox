const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const tasksRouter = require('./routes/tasks');
const sessionsRouter = require('./routes/sessions');
const authRouter = require('./routes/auth');

const session = require('express-session');
const redis = require('redis');

const mongoose = require('mongoose');

mongoose.connect('mongodb://localhost:27017/example', { useNewUrlParser: true, useUnifiedTopology: true });
mongoose.set('debug', true);

const passport = require("passport");
const LocalStrategy = require('passport-local').Strategy;

passport.use(new LocalStrategy(
  (username, password, done) => {
    if (username === "demo" && password === "secret") {
			done(null, { username });
		} else {
		  done(null, false, { message: "Login failure" });
		}
  }
));

passport.serializeUser(function(user, done) {
  done(null, user.username);
});

passport.deserializeUser(function(id, done) {
  done(null, { username: id });
});

const RedisStore = require('connect-redis')(session)
const client = redis.createClient()

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(session({ store: new RedisStore({ client }), secret: "keyboard cat", resave: false }));
app.use(passport.initialize());
app.use(passport.session());

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/tasks', tasksRouter);
app.use('/sessions', sessionsRouter);
app.use('/auth', authRouter);

module.exports = app;
