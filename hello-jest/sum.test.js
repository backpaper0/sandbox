const sum = require('./sum');

test('2 + 3 = 5', () => {
  expect(sum(2, 3)).toBe(5);
});
