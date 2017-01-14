//import 'bootstrap/dist/css/bootstrap.css'

import clickme from './clickme'

import { hello , hoge } from './clickme.js'

  import data from './data'
  console.log(data)
  console.log(hello)
  console.log(hoge())
  console.log(hoge())
clickme()

  var x = { id: 123, name: 'uragami', fn() { console.log('yyy') } }

  function show({ id, fn }) {
    console.log(id)
      fn()
  }

show(x)
