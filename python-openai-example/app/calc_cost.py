from dotenv import load_dotenv
from pathlib import Path
import os
import tiktoken

def main() -> None:
    load_dotenv()

    with open(Path("data") / "gitlog.txt") as file:
        all_lines = file.readlines()

    enc = tiktoken.encoding_for_model(os.environ["EMBEDDING_MODEL"])

    tokens_size = sum([
        len(tokens)
        for tokens in enc.encode_batch(all_lines)
    ])

    price = float(os.environ["EMBEDDING_PRICE"])
    rate = float(os.environ["YEN_PER_DOLLER"])

    total_cost = tokens_size / 1000.0 * price * rate
    print(f"total_cost = {total_cost}円")

if __name__ == "__main__":
    main()
