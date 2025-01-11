from logging import getLogger
from logging.config import dictConfig
from typing import Any

from pydantic_settings import (
    BaseSettings,
    SettingsConfigDict,
)


class LoggingSettings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        env_nested_delimiter="__",
    )

    logging: dict[str, Any] = {}


dictConfig(LoggingSettings().logging)


loggers = [
    getLogger("foo"),
    getLogger("bar"),
    getLogger("baz"),
]

for logger in loggers:
    logger.info("message1")
    logger.debug("message2")
