var express = require('express');
var router = express.Router();

var { ulid } = require('ulid');

const messages = [];

function addMessage(content) {
  const id = ulid();
  messages.push({ id, content });
}

'foo bar baz qux'.split(' ').forEach(addMessage);

router.get('/messages', function(req, res, next) {
  setTimeout(() => {
    res.json(messages);
  }, 500);
});

router.post('/messages', function(req, res, next) {
  const { content } = req.body;
  if (!content) {
    return res.status(400).end();
  }
  addMessage(content);
  setTimeout(() => {
    res.status(204).end();
  }, 500);
});

module.exports = router;
