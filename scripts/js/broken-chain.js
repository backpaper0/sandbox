
const f = () => new Promise((resolve, reject) => {
    setTimeout(() => reject('x'), 1000)
  })

f().catch(error => {
  console.log('1:' + error)
  return Promise.reject(error)
}).catch(error => {
  console.log('2:' + error)
  return Promise.reject(error)
}).catch(error => {
  console.log('3:' + error)
  return Promise.reject(error)
})
