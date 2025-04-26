from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", env_nested_delimiter="__")

    openai_api_key: str
    embedding_model: str = "text-embedding-3-small"
    embedding_dimensions: int = 256

    parallels: int = 4

    src_filepath: str = "in.jsonl"
    dest_filepath: str = "out.jsonl"
    targets: dict[str, str] = {}

    total: int | None = None

    valkey_url: str | None = None


settings = Settings()  # type: ignore
