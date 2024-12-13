/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */
import express from 'express';
import { getLikeCount } from './like-analyser.js';
import { renderChart } from './chart-renderer.js';
import { Browser, launch } from 'puppeteer';

const app = express();
app.use(express.json());


let browser = null;
/**
 * @deprecated
 * @returns {Promise<Browser>}
 */
async function getBrowser() {
    setupBrowser();
    return browser;
}

async function setupBrowser() {
    if (!browser || browser.connected === false) {
        browser = await launch({
            headless: true,
            args: [
                '--no-sandbox',
                '--disable-setuid-sandbox',
                '--disable-gpu',
                '--disable-dev-shm-usage',
                '--no-first-run',
                '--no-zygote',
                '--single-process',
            ],
            userDataDir: "/dev/null"
        })
    }
}

(async () => {
    app.get("/render", async (req, res) => {
        const key = req.query.id;
        if (!key) {
            return res.status(400).json({ error: 'Id is required' });
        }
        try {
            const response = await fetch(`http://localhost:1215/counter/query?id=${key}`, {
                method: "GET"
            });
            const data = await response.json();

            if (!data) {
                return res.status(400).json({ error: 'Data is required' });
            }
            res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
            res.setHeader('Pragma', 'no-cache');
            res.setHeader('Expires', '0');
            res.setHeader('Content-Type', 'image/svg+xml');
            const html = renderChart(data);
            res.send(html);
        } catch (error) {
            console.log(error)
            res.status(500).json({ error: error.message, stack: error.stack });
        }
    });

    app.listen(1125, () => {
        console.log('Server is running on port 1125');
    });

    process.on('exit', async () => {
        if (browser) {
            await browser.close();
        }
    });

    process.on('SIGINT', async () => {
        process.exit();
    });

    process.on('SIGTERM', async () => {
        process.exit();
    });

})();