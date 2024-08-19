from fastapi import FastAPI
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor

from app.chat import router as chat_router
from app.distributed import router as distributed_router
from app.manual_tracing import router as manual_tracing_router

app = FastAPI()


FastAPIInstrumentor.instrument_app(app)


@app.get("/")
async def root():
    return {"message": "Hello World"}


app.include_router(chat_router)
app.include_router(manual_tracing_router)
app.include_router(distributed_router, prefix="/distributed")
