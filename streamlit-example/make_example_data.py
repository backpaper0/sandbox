import json
import random
from datetime import datetime, timedelta

with open("data/log.jsonl", mode="w", encoding="utf-8") as file:
    start = datetime.fromisoformat("2024-07-01T00:00:00")
    end = datetime.fromisoformat("2024-07-31T23:59:59")
    delta = int((end - start).total_seconds())
    for i in range(10000):
        json.dump(
            {
                "timestamp": (
                    start + timedelta(seconds=random.randrange(delta))
                ).isoformat(),
                "response_time": random.randint(1, 10000),
            },
            file,
            ensure_ascii=False,
        )
        file.write("\n")
