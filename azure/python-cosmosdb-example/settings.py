from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    model_config = {"env_file": ".env"}

    cosmos_endpoint: str
    cosmos_key: str | None = None
    cosmos_database_name: str
    cosmos_container_name: str
