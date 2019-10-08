var express = require('express');
var router = express.Router();

const mongoose = require('mongoose');

router.post('/', async function(req, res, next) {

  mongoose.connect('mongodb://localhost:27017/example', { useNewUrlParser: true, useUnifiedTopology: true });
  
  const Task = mongoose.model('Task', { title: String, done: Boolean });
  
	const title = req.body.title;
	const done = false;
  const todo = new Task({ title, done });
  const saved = await todo.save();

  res.send(saved);
});

module.exports = router;
