const passport = require("passport");
const LocalStrategy = require('passport-local').Strategy;
const BearerStrategy = require('passport-http-bearer').Strategy;

passport.use(new LocalStrategy((username, password, done) => {
	if (username === "demo" && password === "secret") {
		const user = { username };
		done(null, user);
		return;
	}
	done(null, false, { message: "Login failure" });
}));

passport.use(new BearerStrategy((token, done) => {
	if (token === "randomtoken") {
		const user = { username: "demo" };
		done(null, user);
		return;
	}
	done(null, false, { message: "Invalid token" });
}));

const local = passport.authenticate("local", { session: false });
const bearer = passport.authenticate("bearer", { session: false });

module.exports = { local, bearer };

