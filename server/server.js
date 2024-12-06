/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */
import express from 'express';
import { getLikeCount } from './like-analyser.js';
import { renderChart } from './chart-renderer.js';
import { launch } from 'puppeteer';

const app = express();
app.use(express.json());

let browser;

(async () => {
    browser = await launch({
        executablePath: '/usr/bin/chromium-browser',
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-gpu',
            '--disable-dev-shm-usage'
        ]
    });

    app.get('/get', async (req, res) => {
        const key = req.query.key;
        if (!key) {
            return res.status(400).json({ error: 'Key is required' });
        }

        try {
            const likeCount = await getLikeCount(browser, key);
            res.json({
                status: 'success',
                likeCount: likeCount
            });
        } catch (error) {
            res.status(500).json({ error: error.message });
        }
    });

    app.get("/render", async (req, res) => {
        const key = req.query.id;
        try {
            const response = await fetch(`http://localhost:1215/counter/query?id=${key}`, {
                method: "GET"
            });
            const data = await response.json();

            if (!data) {
                return res.status(400).json({ error: 'Data is required' });
            }

            const html = renderChart(data);
            res.send(html);
        } catch (error) {
            res.status(500).json({ error: error.message });
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