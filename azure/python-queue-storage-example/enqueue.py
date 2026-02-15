import argparse
import asyncio

from azure.core.exceptions import ResourceExistsError
from azure.identity.aio import DefaultAzureCredential
from azure.storage.queue.aio import QueueClient

from settings import Settings


async def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("message", nargs="?", default="Hello, Azure Queue!")
    args = parser.parse_args()

    settings = Settings()  # type: ignore[missing-argument]
    credential = settings.queue_account_key or DefaultAzureCredential()
    async with QueueClient(
        account_url=settings.queue_account_url,
        queue_name=settings.queue_name,
        credential=credential,
    ) as client:
        try:
            await client.create_queue()
        except ResourceExistsError:
            pass
        await client.send_message(args.message)
        print(f"Sent: {args.message}")


if __name__ == "__main__":
    asyncio.run(main())
