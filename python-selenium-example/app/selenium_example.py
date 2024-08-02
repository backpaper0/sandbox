from pathlib import Path

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By


def main() -> None:
    chrome_options = Options()
    chrome_options.add_experimental_option("debuggerAddress", "127.0.0.1:9222")

    driver_path = Path.home() / "chromedriver"

    service = Service(executable_path=driver_path)
    driver = webdriver.Chrome(service=service, options=chrome_options)

    urls = [driver.current_url]
    traverse(driver, urls)

    for url in urls:
        print(url)


def traverse(driver: webdriver.Chrome, urls: list[str]) -> None:
    links = driver.find_elements(By.TAG_NAME, "a")
    new_urls = [link.get_attribute("href") for link in links]
    for url in new_urls:
        if url is not None and url not in urls:
            urls.append(url)
            driver.get(url)
            traverse(driver, urls)


if __name__ == "__main__":
    main()
