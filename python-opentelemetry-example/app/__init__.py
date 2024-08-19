from dotenv import load_dotenv
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.aiohttp_client import AioHttpClientInstrumentor
from opentelemetry.instrumentation.langchain import LangchainInstrumentor
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.semconv.resource import ResourceAttributes

for suffix in ["local", "secret"]:
    load_dotenv(f".env.{suffix}")

_resource = Resource.create({ResourceAttributes.SERVICE_NAME: "telemetry-example"})

_provider = TracerProvider(resource=_resource)
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
