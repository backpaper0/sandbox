import asyncio
from playwright.async_api import async_playwright

async def run(playwright):
    # ブラウザを起動
    browser = await playwright.chromium.launch(headless=True)
    context = await browser.new_context(
        viewport={"width": 800, "height": 600},
    )

    # 新しいページを開く
    page = await context.new_page()

    # 指定されたURLに移動
    await page.goto("https://example.com")

    # ページのタイトルを取得
    title = await page.title()
    print(f"Page title: {title}")

    # ページ内のすべてのリンクを取得
    links = await page.query_selector_all("a")
    # リンクのhref属性を取得して表示
    for link in links:
        href = await link.get_attribute("href")
        print(href)

    # スクリーンショットを取得して保存
    await page.screenshot(path="screenshot.png")

    # ブラウザを閉じる
    await browser.close()

async def main():
    async with async_playwright() as playwright:
        await run(playwright)

asyncio.run(main())
