import time

from tqdm import trange


def main():
    print("Hello from app!")
    for _ in trange(10):
        time.sleep(0.1)


if __name__ == "__main__":
    main()
