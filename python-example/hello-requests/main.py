import requests


def main():
    resp = requests.get("https://api.github.com/zen")
    print(resp.text)


if __name__ == "__main__":
    main()
