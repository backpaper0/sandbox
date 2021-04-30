const { hello } = require('./hello');

test('helloworld', () => {
  expect(hello('test')).toBe('Hello, test!');
});

