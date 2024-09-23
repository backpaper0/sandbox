# python -m unittest -v tests/test_example.py

import unittest


class TestExample(unittest.TestCase):
    def test_upper(self):
        self.assertEqual("foo".upper(), "FOO")

    def test_lower(self):
        self.assertEqual("BAR".lower(), "bar")
