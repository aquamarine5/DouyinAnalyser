/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */
import express from 'express';
import { getLikeCount } from './analyser.js';
import { renderChart } from './chartrenderer.js';
const app = express()
app.use(express.json())

app.get('/get', async (req, res) => {
    const key = req.query.key;
    if (!key) {
        return res.status(400).json({ error: 'Key is required' });
    }

    try {
        const likeCount = await getLikeCount(key);
        res.json({
            status: 'success',
            likeCount: likeCount
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});
app.post("/render", async (req, res) => {
    const key = req.query.key;
    fetch(`http://localhost:1215/counter/query?key=${key}`)
    if (!data) {
        return res.status(400).json({ error: 'Data is required' });
    }
    try {
        const html = renderChart(data);
        res.send(html);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
})
app.listen(1125, () => {
    console.log('Server is running on port 1125')
})