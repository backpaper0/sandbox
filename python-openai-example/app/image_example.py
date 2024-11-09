"""
poetry run python -m app.image_example
"""

import asyncio

from dotenv import load_dotenv
from openai import AsyncOpenAI
from openai.types.chat import ChatCompletion


async def main() -> None:
    load_dotenv()

    # with open(Path("data") / "avatar.png", mode="rb") as file:
    #     b = base64.b64encode(file.read()).decode("utf-8")
    # url = f"data:image/png;base64,{b}"
    url = "https://avatars.githubusercontent.com/u/209262?v=4"

    client = AsyncOpenAI()

    response: ChatCompletion = await client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {
                "role": "system",
                "content": "あなたは与えられた画像の内容を要約するアシスタントです。",
            },
            {
                "role": "user",
                "content": [
                    {
                        "type": "text",
                        "text": "この画像について教えてください。",
                    },
                    {
                        "type": "image_url",
                        "image_url": {
                            "url": url,
                            "detail": "auto",
                        },
                    },
                ],
            },
        ],
    )

    print(response.choices[0].message.content)
    print()

    yen_per_1k_input = 0.02168
    yen_per_1k_output = 0.0868
    usage = response.usage
    if usage is not None:
        yen_of_input = (usage.prompt_tokens / 1000) * yen_per_1k_input
        yen_of_output = (usage.completion_tokens / 1000) * yen_per_1k_output
        print(f"入力: {yen_of_input}円")
        print(f"出力: {yen_of_output}円")
        print(f"合計: {yen_of_input + yen_of_output}円")


if __name__ == "__main__":
    asyncio.run(main())
