import sum, { fn1 } from './sum';

test('2 + 3 = 5', () => {
  expect(sum(2, 3)).toBe(5);
});

describe('fn1', () => {
  test('foo', () => expect(fn1('foo')).toEqual('FOO'));
  test('bar', () => expect(fn1('bar')).toEqual('BAR'));
});
