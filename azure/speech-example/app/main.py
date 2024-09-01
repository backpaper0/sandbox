import os

from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles

load_dotenv()

app = FastAPI()


@app.get("/api/speech-subscription-key")
def speechSubscriptionKey():
    return {"speechSubscriptionKey": os.getenv("SPEECH_SUBSCRIPTION_KEY")}


app.mount("/", StaticFiles(directory="static"), name="static")
