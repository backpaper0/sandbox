import test from 'ava'
import Hello from './hello'

test.beforeEach(t => {
  t.context = new Hello()
})

test('hello default', t => {
  t.is(t.context.hello(), 'Hello, world!')
})

test('hello with arg', t => {
  t.is(t.context.hello('ava'), 'Hello, ava!')
})
