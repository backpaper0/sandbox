import asyncio

from dotenv import load_dotenv
from openai import OpenAI


async def main() -> None:
    load_dotenv()
    oai = OpenAI()
    models = oai.models.list()
    for model in models:
        print(model.model_dump())


if __name__ == "__main__":
    asyncio.run(main())
