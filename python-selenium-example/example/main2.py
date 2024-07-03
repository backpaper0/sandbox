import os

from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from splinter import Browser
from splinter.driver.webdriver import DriverAPI


def main() -> None:
    chrome_options = Options()
    chrome_options.add_experimental_option("debuggerAddress", "127.0.0.1:9222")

    driver_path = f"{os.environ['HOME']}/chromedriver"

    service = Service(executable_path=driver_path)

    browser = Browser(driver_name="chrome", service=service, options=chrome_options)
    urls = [browser.url]
    traverse(browser, urls)

    for url in urls:
        print(url)


def traverse(browser: DriverAPI, urls: list[str]) -> None:
    links = browser.find_by_tag("a")

    new_urls = [link["href"] for link in links]
    for url in new_urls:
        if url is not None and url not in urls:
            urls.append(url)
            browser.visit(url)
            traverse(browser, urls)


if __name__ == "__main__":
    main()
