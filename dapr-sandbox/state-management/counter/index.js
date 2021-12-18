const express = require('express');
const app = express();
const port = parseInt(process.env.PORT) || 3000;
const fetch = require('node-fetch');

const stateStoreUrl = `http://localhost:${process.env.DAPR_HTTP_PORT}/v1.0/state/statestore`;

app.get('/count', async (req, res) => {
  const count = await (async () => {
    const resp = await fetch(`${stateStoreUrl}/count`);
    console.log('get count state', resp.status);
    if (resp.status === 204) {
      return 1;
    }
    const { count } = await resp.json();
    console.log('currnt count', count);
    return count + 1;
  })();
  const resp = await fetch(stateStoreUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify([{
      key: 'count',
      value: { count },
    }]),
  });
  console.log('save count state', resp.status, 'count = ', count);
  res.send({ count });
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});

