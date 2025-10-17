import shutil
import sys
import tempfile

from pydantic_settings import BaseSettings
from selenium.webdriver import Chrome
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service


class Settings(BaseSettings):
    chrome_path: str = ""
    webdriver_path: str = ""
    url: str = "https://urgm.jp/private_screening_lycoris_recoil.html"


settings = Settings()


def main():
    user_data_dir = tempfile.mkdtemp()

    options = Options()
    # ヘッドレスモード
    options.add_argument("--headless=new")
    # 制限された環境(Docker, AWS Lambdaなど)ではサンドボックスがうまく機能しないため無効化
    options.add_argument("--no-sandbox")
    # 制限された環境では共有メモリの使用で問題が発生することがあるため無効化
    options.add_argument("--disable-dev-shm-usage")
    # 実行ごとに独立した一時プロファイルを使いたいため
    options.add_argument(f"--user-data-dir={user_data_dir}")

    if settings.chrome_path:
        options.binary_location = settings.chrome_path
    if settings.webdriver_path:
        service = Service(executable_path=settings.webdriver_path)
    else:
        service = None

    try:
        chrome = Chrome(options=options, service=service)
        chrome.set_window_size(1280, 720)
        chrome.get(settings.url)
        screenshot = chrome.get_screenshot_as_png()
        sys.stdout.buffer.write(screenshot)
        sys.stdout.buffer.flush()
    finally:
        shutil.rmtree(user_data_dir, ignore_errors=True)


if __name__ == "__main__":
    main()
