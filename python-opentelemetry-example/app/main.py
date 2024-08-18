from base64 import b64encode

from dotenv import load_dotenv
from fastapi import FastAPI
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor, ConsoleSpanExporter
from opentelemetry.semconv.resource import ResourceAttributes

load_dotenv(".env.local")

app = FastAPI()

resource = Resource.create({ResourceAttributes.SERVICE_NAME: "telemetry-example"})

provider = TracerProvider(resource=resource)
trace.set_tracer_provider(provider)

# exporter1 = OTLPSpanExporter(
#     endpoint="http://localhost:5080/api/default/v1/traces",
#     headers={
#         "Authorization": f"Basic {b64encode(b'root@example.com:pass1234').decode()}"
#     },
# )
exporter1 = OTLPSpanExporter()
processor1 = BatchSpanProcessor(exporter1)
provider.add_span_processor(processor1)

exporter2 = ConsoleSpanExporter()
processor2 = BatchSpanProcessor(exporter2)
provider.add_span_processor(processor2)

FastAPIInstrumentor.instrument_app(app)


@app.get("/")
async def root():
    return {"message": "Hello World"}
