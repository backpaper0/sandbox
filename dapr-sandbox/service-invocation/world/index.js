const express = require('express');
const app = express();
const port = parseInt(process.env.PORT) || 3001;

app.get('/world', (req, res) => {
  const env = process.env;
  const headers = req.headers;
  const message = 'Dapr world';
  res.json({ message, env, headers });
});


app.get('/env', async (req, res) => {
  const env = process.env;
  res.json({ env });
});

app.get('/headers', async (req, res) => {
  const headers = req.headers;
  res.json({ headers });
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});

