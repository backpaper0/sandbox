from dotenv import load_dotenv

for suffix in ["local", "secret"]:
    load_dotenv(f".env.{suffix}")
