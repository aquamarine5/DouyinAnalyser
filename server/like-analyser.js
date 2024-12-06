/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

import { Browser } from "puppeteer";

/**
 * 
 * @param {Browser} browser 
 * @param {string} key 
 * @returns 
 */
export async function getLikeCount(browser, key) {
    // 启动浏览器
    const page = await browser.newPage();

    // 拦截网络请求
    await page.setRequestInterception(true);
    page.on('request', interceptedRequest => {
        if (interceptedRequest.resourceType() === 'image') {
            interceptedRequest.abort(); // 阻止图片请求
        } else {
            interceptedRequest.continue();
        }
    });

    // 监听网络响应
    return new Promise((resolve, reject) => {
        page.on('response', async response => {
            const responseUrl = response.url();
            // 检查 URL 是否包含 'other'
            if (responseUrl.includes('www.douyin.com/aweme/v1/web/user/profile/other')) {
                try {
                    // 获取响应内容
                    const responseBody = await response.text();
                    const likeCount = JSON.parse(responseBody).user.favoriting_count;
                    resolve(likeCount);
                    await page.close();
                } catch (error) {
                    reject(error);
                    await page.close();
                }
            }
        });

        // 访问页面
        page.goto(`https://www.douyin.com/user/${key}`).catch(reject);
    });
}