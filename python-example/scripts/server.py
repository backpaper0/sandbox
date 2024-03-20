# docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin
# poetry run task serve
# curl localhost:8000/rolldice

from fastapi import FastAPI
from random import randint
from typing import Union
import logging

app = FastAPI()
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.get("/rolldice")
def roll_dice(player: Union[str, None] = None) -> str:
    result = str(_roll())
    if player:
        logger.warning("%s is rolling the dice: %s", player, result)
    else:
        logger.warning("Anonymous player is rolling the dice: %s", result)
    return result

def _roll() -> int:
    res = randint(1, 6)
    return res



from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
from opentelemetry import trace
from opentelemetry.exporter.zipkin.proto.http import ZipkinExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.sdk.resources import SERVICE_NAME, Resource

resource = Resource(attributes={
    SERVICE_NAME: "dice-server"
})

traceProvider = TracerProvider(resource=resource)
processor = BatchSpanProcessor(ZipkinExporter())
traceProvider.add_span_processor(processor)
trace.set_tracer_provider(traceProvider)

FastAPIInstrumentor.instrument_app(app)