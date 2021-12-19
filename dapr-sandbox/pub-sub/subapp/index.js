const express = require('express');
const app = express();
const port = parseInt(process.env.PORT) || 3001;

//app.use(express.json());
app.use((req, res, next) => {
  let data = "";

  req.setEncoding("utf8");
  req.on("data",  (chunk) => {
    data += chunk;
  });

  req.on("end", () => {
    if (data) {
      req.body = JSON.parse(data);
    }
    next();
  });
});

app.post('/subscribe', async (req, res) => {
  console.log(req.headers);
  console.log(req.body);
  res.status(200).end();
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});

