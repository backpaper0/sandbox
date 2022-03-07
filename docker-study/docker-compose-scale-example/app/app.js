const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
const os = require('os');

const hostname = os.hostname();
const message = process.env.MESSAGE || 'Hello World';
const appPath = process.env.APP_PATH || '/';

app.get(appPath, (req, res) => {
  console.log('handle reqest');
  res.json({
    message,
    hostname,
  })
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})
