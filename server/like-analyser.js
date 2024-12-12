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
    const validKeyPattern = /^[a-zA-Z0-9_-]+$/;
    if (!validKeyPattern.test(key)) {
        throw new Error('Invalid key parameter');
    }

    const page = await browser.newPage();

    await page.setRequestInterception(true);
    page.on('request', async interceptedRequest => {
        if ([].includes(interceptedRequest.resourceType())) {
            await interceptedRequest.abort();
        } else {
            await interceptedRequest.continue();
        }
    });

    return new Promise((resolve, reject) => {
        page.on('response', async response => {
            const responseUrl = response.url();
            console.log(responseUrl);
            const responseType = response.request().method();
            if (responseUrl.includes('aweme/v1/web/user/profile/other') && responseType === 'GET') {
                try {
                    const responseBody = JSON.parse(await response.text());
                    if (responseBody.user.favorite_permission == 1) {
                        resolve(-1);
                    } else {
                        resolve(responseBody.user.favoriting_count);
                    }
                } catch (error) {
                    reject(error);
                } finally {
                    await page.close();
                }
            }
        });

        page.goto(`https://www.douyin.com/user/${key}`, { timeout: 3000000 }).catch(async error => {
            await page.close();
            reject(error);
        });
    });
}