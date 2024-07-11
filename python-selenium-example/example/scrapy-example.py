import scrapy
from scrapy.http import TextResponse
from scrapy.spiders import Request, Response, Spider
from scrapy.utils.decorators import defers


class Handler:
    @defers
    def download_request(self, request: Request, spider: Spider) -> Response:
        return TextResponse(request.url, body=f"{request.url}".encode())


class QuotesSpider(scrapy.Spider):
    name = "quotes"
    start_urls = [
        "https://example.com/",
    ]
    custom_settings = {
        "DOWNLOAD_HANDLERS": {
            "http": Handler,
            "https": Handler,
        },
        "LOG_LEVEL": "WARNING",
        # "DOWNLOAD_DELAY": 1,
    }

    def parse(self, response: Response):
        if response.url == "https://example.com/":
            for i in range(1, 5):
                yield response.follow(f"{response.url}{i}", self.parse)
        elif response.url == "https://example.com/1":
            for i in range(1, 5):
                yield response.follow(f"/1/{i}", self.parse)
        elif response.url == "https://example.com/2":
            for i in range(1, 5):
                yield response.follow(f"https://two.example.com/2/{i}", self.parse)
        elif response.url == "https://example.com/3":
            for i in range(1, 5):
                yield response.follow(f"/3/{i}", self.parse)
        elif response.url == "https://example.com/3/1":
            for i in range(1, 5):
                yield response.follow(f"/3/1/{i}", self.parse)
        elif response.url == "https://example.com/3/1/1":
            yield response.follow(f"/3/1/1/1", self.parse)
        elif response.url == "https://example.com/3/1/1/1":
            yield response.follow(f"/3/1/1/1/1", self.parse)
        elif response.url == "https://example.com/3/1/1/1/1":
            yield response.follow(f"/3/1/1/1/1/1", self.parse)
        elif response.url == "https://example.com/3/1/1/1/1/1":
            yield response.follow(f"/3/1/1/1/1/1/1", self.parse)
        url: str = response.urljoin(response.url)
        if url.startswith("https://example.com/"):
            yield {
                "url": url,
            }
