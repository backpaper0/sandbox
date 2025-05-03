from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.sdk.resources import Resource
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter


def process_tracing(resource: Resource) -> None:
    span_exporter = OTLPSpanExporter()

    tracer_provider = TracerProvider(resource=resource)
    span_processor = BatchSpanProcessor(span_exporter)
    tracer_provider.add_span_processor(span_processor)
    trace.set_tracer_provider(tracer_provider)

    tracer = trace.get_tracer("demo-tracer")
    with tracer.start_as_current_span("main-operation") as main_span:
        main_span.set_attribute("app.section", "initialization")

        with tracer.start_as_current_span("child-task") as child_span:
            child_span.add_event("An important event happened")
            child_span.set_attribute("task.detail", "processing data")

    # TracerProviderをシャットダウンしてすべてのspanをエクスポート
    tracer_provider.shutdown()
