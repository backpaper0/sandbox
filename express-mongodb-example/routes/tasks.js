var express = require('express');
var router = express.Router();

const mongoose = require('mongoose');
const Task = require("../models/Task");

router.post('/', async function(req, res, next) {

	const title = req.body.title;
	const done = false;
  const todo = new Task({ title, done });
  const saved = await todo.save();

  res.send(saved);
});

module.exports = router;
