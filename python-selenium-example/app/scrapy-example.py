import time
from pathlib import Path

import scrapy
from scrapy.http import HtmlResponse
from scrapy.spiders import Request, Response, Spider
from scrapy.utils.decorators import defers
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By


class Handler:

    driver: webdriver.Chrome

    def __init__(self):
        chrome_options = Options()
        chrome_options.add_experimental_option("debuggerAddress", "127.0.0.1:9222")

        driver_path = Path.home() / "chromedriver"

        service = Service(executable_path=driver_path)
        self.driver = webdriver.Chrome(service=service, options=chrome_options)

    @defers
    def download_request(self, request: Request, spider: Spider) -> Response:
        driver = self.driver
        driver.get(request.url)
        time.sleep(1)  # 雑に描画の完了を待機
        resp = HtmlResponse(request.url)
        html = driver.find_element(By.TAG_NAME, "html").get_attribute("outerHTML")
        body = html.encode() if isinstance(html, str) else b""
        return HtmlResponse(url=request.url, body=body)


class ExampleSpider(scrapy.Spider):
    name = "example"
    start_urls = [
        "http://localhost:5173",
    ]
    custom_settings = {
        "DOWNLOAD_HANDLERS": {
            "http": Handler,
            "https": Handler,
        },
        "LOG_LEVEL": "WARNING",
    }

    def parse(self, response: Response):
        links = response.css("a::attr(href)")
        for link in links:
            url = link.extract()
            yield response.follow(url, self.parse)
        yield {
            "url": response.url,
            "body": response.body.decode(),
        }
