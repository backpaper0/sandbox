from fastapi import FastAPI
from .reverse_proxy.reverse_proxy import router as reverse_proxy_router

app = FastAPI()

app.include_router(reverse_proxy_router, prefix="/reverse-proxy")
