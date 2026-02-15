import asyncio

from azure.identity.aio import DefaultAzureCredential
from azure.storage.queue.aio import QueueClient

from settings import Settings


async def main():
    settings = Settings()  # type: ignore[missing-argument]
    credential = settings.queue_account_key or DefaultAzureCredential()
    async with QueueClient(
        account_url=settings.queue_account_url,
        queue_name=settings.queue_name,
        credential=credential,
    ) as client:
        message = await client.receive_message()
        if message:
            print(f"Received: {message.content}")
            # receive_message() はメッセージを一時的に不可視にするだけで、キューからは削除しない。
            # 明示的に delete しないと visibility timeout（デフォルト30秒）後にキューへ再出現する。
            # visibility timeout は receive_message(visibility_timeout=秒数) で 1〜604800秒（7日）に変更可能。
            await client.delete_message(message)
        else:
            print("No messages in the queue.")


if __name__ == "__main__":
    asyncio.run(main())
