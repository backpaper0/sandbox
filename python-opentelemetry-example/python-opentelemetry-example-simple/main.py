from dotenv import load_dotenv
from opentelemetry.sdk.resources import Resource
from tracing import process_tracing
from metrics import process_metrics
from logs import process_logs

load_dotenv()


def main() -> None:
    resource = Resource.create(
        {
            "service.name": "demo-service",
            "service.instance.id": "instance-1",
            "service.version": "1.0.0",
            "host.name": "localhost",
        }
    )

    process_logs(resource)
    process_metrics(resource)
    process_tracing(resource)


if __name__ == "__main__":
    main()
