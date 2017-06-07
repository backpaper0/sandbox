import test from 'ava'
import hello from './hello.js'

test('hello', t => {
  t.is(hello('world'), 'Hello, world!')
})
