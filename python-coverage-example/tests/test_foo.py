import unittest

import app.foobar as fb


class TestFoo(unittest.TestCase):
    def test_foo(self):
        self.assertEqual(fb.foo("abc"), "ABC")
