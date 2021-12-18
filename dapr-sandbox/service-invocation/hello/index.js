const express = require('express');
const app = express();
const port = parseInt(process.env.PORT) || 3000;
const fetch = require('node-fetch');

const worldUrl = (() => {
  const daprPort = process.env.DAPR_HTTP_PORT;
  if (daprPort) {
    return `http://localhost:${daprPort}/v1.0/invoke/world-svc/method`;
  }
  return process.env.WORLD_URL || 'http://localhost:3001';
})();

app.get('/hello', async (req, res) => {
  const { message: world } = await fetch(`${worldUrl}/world`).then(a => a.json());
  const message = `Hello, ${world}!`;
  res.json({ message });
});

app.get('/env', async (req, res) => {
  const helloEnv = process.env;
  const { env: worldEnv } = await fetch(`${worldUrl}/env`).then(a => a.json());
  res.json({ helloEnv, worldEnv });
});

app.get('/headers', async (req, res) => {
  const helloHeaders = req.headers;
  const { headers: worldHeaders } = await fetch(`${worldUrl}/headers`).then(a => a.json());
  res.json({ helloHeaders, worldHeaders });
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});

