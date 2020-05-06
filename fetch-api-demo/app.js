const express = require('express');
const app = express();

app.get('/', (req, res) => res.sendFile('index.html', { root: './' }));
app.get('/index.js', (req, res) => res.sendFile('index.js', { root: './', headers: { 'Content-Type': 'application/javascript' } }));

app.get('/methods/get', (req, res) => res.send('Hello, GET request!'));
app.head('/methods/head', (req, res) => res.send('Hello, HEAD request!'));
app.post('/methods/post', (req, res) => res.send('Hello, POST request!'));
app.put('/methods/put', (req, res) => res.send('Hello, PUT request!'));
app.delete('/methods/delete', (req, res) => res.send('Hello, DELETE request!'));
app.trace('/methods/trace', (req, res) => res.send('Hello, TRACE request!'));
app.options('/methods/options', (req, res) => res.send('Hello, OPTIONS request!'));
app.connect('/methods/connect', (req, res) => res.send('Hello, CONNECT request!'));
app.patch('/methods/patch', (req, res) => res.send('Hello, PATCH request!'));

app.get('/abort', (req, res) => {
  console.log('[abort]Begin abort example');
  setTimeout(() => {
    console.log('[abort]...');
    res.json({ message: 'hello world' });
    console.log('[abort]End abort example');
  }, 5000);
});

app.listen(3000, () => console.log('Example app listening on port 3000!'));

