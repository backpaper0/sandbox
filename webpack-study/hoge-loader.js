module.exports = function (content) {
  for(let i = 0; i < 10; i++) {
    console.log(content)
  }
  return 'module.exports = function() { console.log("Hello, world!") }'
}
