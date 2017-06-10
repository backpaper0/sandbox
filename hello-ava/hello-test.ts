import test from 'ava'
import Hello from './hello'

test('hello default', t => {
  const a = new Hello()
  t.is(a.hello(), 'Hello, world!')
})

test('hello with arg', t => {
  const a = new Hello()
  t.is(a.hello('ava'), 'Hello, ava!')
})
