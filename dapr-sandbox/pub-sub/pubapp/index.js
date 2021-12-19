const express = require('express');
const app = express();
const port = parseInt(process.env.PORT) || 3000;
const fetch = require('node-fetch');

const publishUrl = `http://localhost:${process.env.DAPR_HTTP_PORT}/v1.0/publish/pubsub/demo`;

app.use(express.urlencoded({ extended: true }))

app.post('/publish', async (req, res) => {
  console.log(req.body);
  await fetch(publishUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      message: req.body.message,
      timestamp: new Date().toISOString(),
    }),
  });
  res.end();
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});

