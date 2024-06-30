const express = require('express');
const app = express();

app.use(express.static('./'));

['get', 'head', 'post', 'put', 'delete', 'trace', 'options', 'connect', 'patch'].forEach((method) => {
  app[method]('/methods', (req, res) => res.send(`Hello, ${method} request!`));
});

app.get('/abort', (req, res) => {
  setTimeout(() => {
    res.send('Hello World');
  }, 5000);
});

[200, 302, 400, 401, 403, 404, 500, 503].forEach(status => {
  app.get(`/status/${status}`, (req, res) => {
    if (300 <= status && status <= 399) {
      res.redirect('/status/200');
      return;
    }
    res.status(status).end();
  });
});

app.listen(3000, () => console.log('Example app listening on port 3000!'));
