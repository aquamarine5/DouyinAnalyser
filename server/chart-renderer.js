/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */
import * as echarts from 'echarts/core';
import { SVGRenderer } from 'echarts/renderers';
import { BarChart, LineChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
import { JSDOM } from 'jsdom';

export function renderChart(data) {
    // 创建虚拟 DOM 环境
    const { window } = new JSDOM('<!DOCTYPE html><html><body><div id="chart"></div></body></html>');
    window.HTMLCanvasElement.prototype.getContext = () => {
        // return whatever getContext has to return
    };
    global.window = window;
    global.document = window.document;

    // 准备容器
    const div = document.getElementById('chart');
    Object.defineProperties(div, {
        clientWidth: { value: 600 },
        clientHeight: { value: 400 }
    });

    echarts.use([
        SVGRenderer,
        BarChart,
        TitleComponent,
        TooltipComponent,
        LegendComponent,
        GridComponent,
        LineChart
    ]);
    console.log(data)
    let datalist = data.data.list

    const chart = echarts.init(div, null, {
        renderer: 'svg',
        width: 600,
        height: 400
    });
    let categories = []
    for (let i = 0; i < datalist.length; i++) {
        categories.push(datalist[i].date)
    }
    let values = []
    for (let i = 0; i < datalist.length; i++) {
        values.push(datalist[i].count)
    }
    const yValues = datalist.map(item => item.count);
    const minValue = Math.min(...yValues);
    const option = {
        title: {
            text: '数据统计'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            data: categories
        },
        yAxis: {
            type: 'value',
            min: Math.floor(minValue * 0.98),
            minInterval: 10
        },
        series: [{
            type: 'line',
            data: values,
            smooth: true
        }],
        animation: true
    };

    chart.setOption(option);

    const svgStr = div.querySelector('svg').outerHTML;


    chart.dispose();
    delete global.window;
    delete global.document;

    return svgStr;
}