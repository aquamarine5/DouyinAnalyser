/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */
import { launch } from 'puppeteer';

(async () => {
    // 启动浏览器
    const browser = await launch({
        headless: false, // 设置为 false 表示打开真实浏览器
    });
    const page = await browser.newPage();

    // 拦截网络请求
    await page.setRequestInterception(true);
    page.on('request', interceptedRequest => {
        interceptedRequest.continue();
    });

    // 监听网络响应
    page.on('response', async response => {
        const url = response.url();
        // 检查 URL 是否包含 'other'
        if (url.includes('www.douyin.com/aweme/v1/web/user/profile/other')) {
            // 获取响应内容
            const responseBody = await response.text();
            console.log(`URL: ${url}`);
            console.log(`Response: ${responseBody}`);
            console.log("Like Count: ", JSON.parse(responseBody).user.
                favoriting_count
            );
            // 在这里可以根据需要处理响应
        }
    });

    // 导航到目标网页
    await page.goto('https://www.douyin.com/user/MS4wLjABAAAApuyqymIaQkpvKkbdH1X6W3A6XEgJl7kddGrZHxipJ7TbA1lCRaPJK5gZ1KX7pR1n');

    // 由于浏览器是开放的，你可以在此进行交互

    // 关闭浏览器（根据需要选择何时关闭）
    // await browser.close();
})();