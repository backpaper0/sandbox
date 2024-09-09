import asyncio
import os

from azure.storage.blob.aio import BlobServiceClient
from dotenv import load_dotenv


async def main():
    load_dotenv()
    account_url = os.environ["BLOB_ACCOUNT_URL"]
    credential = os.environ["BLOB_ACCOUNT_KEY"]

    async with BlobServiceClient(
        account_url, credential=credential
    ) as blob_service_client:
        container_client = blob_service_client.get_container_client(
            container="sample-container"
        )
        if not (await container_client.exists()):
            await container_client.create_container()
        await container_client.upload_blob(name="hello.txt", data="Hello, World!")
        await container_client.upload_blob(
            name="foo/bar/baz.txt", data="こんにちは、世界！"
        )


asyncio.run(main())
