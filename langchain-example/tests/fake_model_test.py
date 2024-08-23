"""
モックを試す。

python -m tests.fake_model_test -v
"""

import unittest

from langchain_community.chat_models.fake import FakeListChatModel
from langchain_community.embeddings.fake import DeterministicFakeEmbedding


class FakeListChatModelTest(unittest.TestCase):

    def setUp(self) -> None:
        # 指定した応答を順番に返すチャットモデルのモック
        self.chat = FakeListChatModel(
            responses=["foo", "bar"],
            sleep=0.1,
        )

    def test_invoke(self) -> None:
        msg1 = self.chat.invoke("hello")
        self.assertEqual(msg1.content, "foo")

        msg2 = self.chat.invoke("hello")
        self.assertEqual(msg2.content, "bar")

        msg3 = self.chat.invoke("hello")
        self.assertEqual(msg3.content, "foo")


class DeterministicFakeEmbeddingTest(unittest.TestCase):

    def setUp(self) -> None:
        # 同じ入力に対して同じ出力を返す埋め込みのモック
        self.embedding = DeterministicFakeEmbedding(size=10)

    def test_embed_query(self) -> None:
        emb1 = self.embedding.embed_query("hello")
        emb2 = self.embedding.embed_query("world")
        emb3 = self.embedding.embed_query("hello")
        self.assertNotEqual(emb1, emb2)
        self.assertEqual(emb1, emb3)


if __name__ == "__main__":
    unittest.main()
