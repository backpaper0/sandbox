import asyncio

from pydantic_ai import Agent


async def main() -> None:
    agent = Agent(
        "ollama:gpt-oss:20b",
        instructions="Be concise, reply with one sentence.",
    )

    result = await agent.run('Where does "hello world" come from?')
    print(result.output)


if __name__ == "__main__":
    asyncio.run(main())
