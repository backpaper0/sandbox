from opentelemetry import metrics
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.sdk.resources import Resource
from opentelemetry.exporter.otlp.proto.http.metric_exporter import OTLPMetricExporter


def process_metrics(resource: Resource) -> None:
    metric_exporter = OTLPMetricExporter()

    metric_reader = PeriodicExportingMetricReader(exporter=metric_exporter)

    provider = MeterProvider(resource=resource, metric_readers=[metric_reader])
    metrics.set_meter_provider(provider)

    meter = metrics.get_meter("demo-meter")

    counter = meter.create_counter("demo_counter")
    counter.add(10, attributes={"item": "apple"})
    counter.add(5, attributes={"item": "banana"})

    hist = meter.create_histogram("demo_histogram")
    hist.record(0.95, attributes={"status": "success"})
    hist.record(1.10, attributes={"status": "error"})

    # MeterProviderをシャットダウンしてすべてのメトリクスをエクスポート
    provider.shutdown()
