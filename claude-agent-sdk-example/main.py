import asyncio

from claude_agent_sdk import ClaudeAgentOptions, query


async def main():
    options = ClaudeAgentOptions(
        system_prompt="You are an expert Python developer",
        permission_mode="acceptEdits",
        cwd="/home/user/project",
    )

    async for message in query(prompt="Create a Python web server", options=options):
        print(message)


if __name__ == "__main__":
    asyncio.run(main())
