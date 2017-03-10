const marked = require('marked')
const fs = require('fs')

fs.readFile('sample.md', 'UTF-8', (err, data) => {
  if (err) throw err
  const lexer = new marked.Lexer()
  const tokens = lexer.lex(data)
  console.log(tokens)
  console.log(lexer.rules)
})
