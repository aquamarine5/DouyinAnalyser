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
    // Validate the key parameter
    const validKeyPattern = /^[a-zA-Z0-9_-]+$/;
    if (!validKeyPattern.test(key)) {
        throw new Error('Invalid key parameter');
    }

    const page = await browser.newPage();

    await page.setRequestInterception(true);
    page.on('request', interceptedRequest => {
        if (interceptedRequest.resourceType() === 'image') {
            interceptedRequest.abort();
        } else {
            interceptedRequest.continue();
        }
    });

    return new Promise((resolve, reject) => {
        page.on('response', async response => {
            const responseUrl = response.url();
            if (responseUrl.includes('www.douyin.com/aweme/v1/web/user/profile/other')) {
                try {
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

        page.goto(`https://www.douyin.com/user/${key}`).catch(reject);
    });
}