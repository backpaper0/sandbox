const passport = require("passport");
const LocalStrategy = require('passport-local').Strategy;

passport.use(new LocalStrategy((username, password, done) => {
	if (username === "demo" && password === "secret") {
		const user = { username };
		done(null, user);
		return;
	}
	done(null, false, { message: "Login failure" });
}));

module.exports = passport.authenticate("local", { session: false });

