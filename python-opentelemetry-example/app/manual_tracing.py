from fastapi import APIRouter
from opentelemetry import trace

router = APIRouter()


@router.get("/manual_tracing")
async def manual_tracing():
    tracer = trace.get_tracer("manual.tracer")
    with tracer.start_as_current_span("manual.tracing.foo") as span1:
        span1.set_attribute("foobar", "foo")
        with tracer.start_as_current_span("manual.tracing.bar") as span2:
            span2.set_attribute("foobar", "bar")
            with tracer.start_as_current_span("manual.tracing.baz") as span3:
                span3.set_attribute("foobar", "baz")
    with tracer.start_as_current_span("manual.tracing.qux") as span4:
        span4.set_attribute("foobar", "qux")
        return {"message": "Manual Tracing"}
