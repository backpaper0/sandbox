import logging
from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
from opentelemetry.exporter.otlp.proto.http._log_exporter import OTLPLogExporter
from opentelemetry.sdk.resources import Resource


def process_logs(resource: Resource) -> None:
    logger_provider = LoggerProvider(resource=resource)

    log_exporter = OTLPLogExporter()
    logger_provider.add_log_record_processor(BatchLogRecordProcessor(log_exporter))

    handler = LoggingHandler(level=logging.INFO, logger_provider=logger_provider)
    logging.getLogger().addHandler(handler)
    logging.getLogger().setLevel(logging.INFO)

    logging.info("Application started")
    logging.warning("An example warning message")
    logging.error("An error occurred in the process")

    # LoggerProviderをシャットダウンしてすべてのログをエクスポート
    logger_provider.shutdown()
