import unittest

import app.foobar as fb


class TestBar(unittest.TestCase):
    def test_bar(self):
        self.assertEqual(fb.bar(1, 2), 3)
