var sinon = require("sinon");

test("hello", () => {

  var f = sinon.fake.returns("hello");
  expect(f()).toBe("hello");
  expect(f.callCount).toBe(1);

});
