from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    model_config = {"env_file": ".env"}

    queue_account_url: str
    queue_account_key: str | None = None
    queue_name: str
