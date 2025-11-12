import asyncio

from claude_agent_sdk import ClaudeAgentOptions, query


async def main():
    options = ClaudeAgentOptions(
        system_prompt="You are an expert Python developer",
        permission_mode="acceptEdits",
        allowed_tools=["Write(hello.py)"],
    )

    async for message in query(
        prompt=(
            "Hello, world!を出力するコードを書いて。"
            "main関数を定義すること。"
            "worldの部分はコマンドライン引数で変更できること。"
            "コードはhello.pyへ保存して。"
        ),
        options=options,
    ):
        print(message)


if __name__ == "__main__":
    asyncio.run(main())
