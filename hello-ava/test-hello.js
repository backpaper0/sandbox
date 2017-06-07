import test from 'ava'
import Hello from './hello.js'

test('hello', t => {
  const a = new Hello()
  t.is(a.hello('world'), 'Hello, world!')
})
