import unittest
from fastapi.testclient import TestClient
from main import app

class TestMain(unittest.TestCase):

    def test_read_root(self):
        with TestClient(app) as client:
            client.get("/")
