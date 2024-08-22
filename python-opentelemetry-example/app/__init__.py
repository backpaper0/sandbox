from typing import Optional, Sequence

from dotenv import load_dotenv
from opentelemetry import trace
from opentelemetry.context import Context
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.aiohttp_client import AioHttpClientInstrumentor
from opentelemetry.instrumentation.langchain import LangchainInstrumentor
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.sdk.trace.sampling import Decision, Sampler, SamplingResult
from opentelemetry.semconv.resource import ResourceAttributes
from opentelemetry.trace.span import TraceState
from opentelemetry.util.types import Attributes

for suffix in ["local", "secret"]:
    load_dotenv(f".env.{suffix}")


class SseOmittingSampler(Sampler):
    def should_sample(
        self,
        parent_context: Optional[Context],
        trace_id: int,
        name: str,
        kind: Optional[trace.SpanKind] = None,
        attributes: Optional[Attributes] = None,
        links: Optional[Sequence[trace.Link]] = None,
        trace_state: Optional[TraceState] = None,
    ) -> SamplingResult:

        # Server-Sent Eventsの場合はサンプリングしない
        if name == "POST /chat/stream http send":
            return SamplingResult(Decision.DROP)

        return SamplingResult(Decision.RECORD_AND_SAMPLE, {"xxx": "yyy"})

    def get_description(self) -> str:
        return "..."


_resource = Resource.create({ResourceAttributes.SERVICE_NAME: "telemetry-example"})

_provider = TracerProvider(resource=_resource, sampler=SseOmittingSampler())
trace.set_tracer_provider(_provider)

# _exporter = OTLPSpanExporter(
#     endpoint="http://localhost:5080/api/default/v1/traces",
#     headers={
#         "Authorization": f"Basic {b64encode(b'root@example.com:pass1234').decode()}"
#     },
# )
_exporter = OTLPSpanExporter()
_processor = BatchSpanProcessor(_exporter)
_provider.add_span_processor(_processor)


_langchain_instrumentor = LangchainInstrumentor()
if not _langchain_instrumentor.is_instrumented_by_opentelemetry:
    _langchain_instrumentor.instrument()

_aio_http_client_instrumentor = AioHttpClientInstrumentor()
if not _aio_http_client_instrumentor.is_instrumented_by_opentelemetry:
    _aio_http_client_instrumentor.instrument()
