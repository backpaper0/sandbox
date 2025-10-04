import os

import requests


def main():
    url = os.getenv("REQUEST_URL", "https://httpsserver.test")
    response = requests.get(url)
    print(response.text)


if __name__ == "__main__":
    main()
