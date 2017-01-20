const f = new Promise(resolve => setTimeout(() => resolve('hello'), 1000))

f.then(x => x + ' world')
  .then(x => console.log(x))

