from fastapi import APIRouter
from opentelemetry import trace

router = APIRouter()


@router.get("/")
async def traceid():
    span = trace.get_current_span()
    span_ctx = span.get_span_context()
    trace_id = span_ctx.trace_id
    formatted_trace_id = trace.format_trace_id(trace_id)
    return {
        "trace_id": trace_id,
        "formatted_trace_id": formatted_trace_id,
    }
