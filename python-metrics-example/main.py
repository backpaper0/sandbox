import random
import time

from opentelemetry import metrics
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.exporter.otlp.proto.http.metric_exporter import OTLPMetricExporter


def main():
    exporter = OTLPMetricExporter(
        endpoint="http://localhost:9090/api/v1/otlp/v1/metrics",
    )
    reader = PeriodicExportingMetricReader(exporter, export_interval_millis=5000)
    provider = MeterProvider(metric_readers=[reader])
    metrics.set_meter_provider(provider)

    meter = metrics.get_meter("example.meter")

    # Counter: 単調増加する値（リクエスト数など）
    request_counter = meter.create_counter(
        "request_count",
        description="リクエスト数",
        unit="1",
    )

    # Gauge: 現在の状態を表す値（CPU使用率、接続数など）
    active_connections = meter.create_gauge(
        "active_connections",
        description="アクティブな接続数",
        unit="1",
    )

    # Histogram: 値の分布を記録（レイテンシ、サイズなど）
    response_time = meter.create_histogram(
        "response_time",
        description="レスポンスタイム",
        unit="ms",
    )

    i = 0
    try:
        while True:
            i += 1
            request_counter.add(1, {"endpoint": "/api/hello"})
            active_connections.set(random.randint(5, 20), {"server": "web-1"})
            response_time.record(random.uniform(10, 500), {"endpoint": "/api/hello"})
            print(f"イテレーション {i}")
            time.sleep(1)
    except KeyboardInterrupt:
        print("停止します...")
    finally:
        provider.shutdown()


if __name__ == "__main__":
    main()
