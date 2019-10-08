const express = require('express')
const app = express()
const port = 3000

app.get('/', (req, res) => res.send('Hello World!'))

app.get('/users/:userId/books/:bookId', function (req, res) {
  res.send(req.params)
})

app.listen(port, () => console.log(`Example app listening on port ${port}!`))
