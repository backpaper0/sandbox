from pydantic_ai import Agent

agent = Agent(  
    'ollama:gpt-oss:20b',
    instructions='Be concise, reply with one sentence.',  
)

result = agent.run_sync('Where does "hello world" come from?')  
print(result.output)
