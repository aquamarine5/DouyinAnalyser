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
        clientWidth: { value: 450 },
        clientHeight: { value: 300 }
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
        width: 450,
        height: 300
    });
    let categories = []
    for (let i = 0; i < datalist.length; i++) {
        let date = datalist[i].date
        let month = Math.floor((date % 10000) / 100)
        let day = date % 100

        month = month < 10 ? '0' + month : month
        day = day < 10 ? '0' + day : day

        categories.push(`${month}/${day}`)
    }
    let values = []
    for (let i = 0; i < datalist.length; i++) {
        values.push(datalist[i].count)
    }
    const yValues = datalist.map(item => item.count);
    const minValue = Math.min(...yValues);
    const maxValue = Math.max(...yValues);
    let deltavalues = []
    for (let i = 1; i < values.length; i++) {
        deltavalues.push(values[i] - values[i - 1])
    }
    const deltaMinValues = Math.min(...deltavalues);
    const deltaMaxValues = Math.max(...deltavalues);
    deltavalues.push('-')
    const option = {
        backgroundColor: '#ffffff',
        title: {
            top: 10,
            left: 10,
            padding: [4, 4],
            textStyle: {
                color: '#2f80ed',
                fontSize: 18,
            },
            text: `@${data.data.name} 的抖音点赞数统计`
        },
        grid: {
            top: 70,
            left: 60,
            right: 60,
            bottom: 20,
            show: true,
            backgroundColor: "#ffffff",
            borderRadius: 8
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            data: categories
        },
        yAxis: [{
            name: "总共",
            type: 'value',
            position: 'left',
            min: Math.floor(minValue * 0.98),
            max: Math.floor(maxValue * 1.02),
            minInterval: 10
        }, {
            name: "当日",
            type: 'value',
            position: 'right',
            max: Math.floor(deltaMaxValues * 1.1),
            min: Math.floor(deltaMinValues * 0.9),
            splitLine: {
                show: false
            }
        }],
        series: [{
            type: 'line',
            data: values,
            smooth: true,
            label: {
                show: true,
                position: 'top',
                fontSize: 12,
                color: '#2f4554',
                formatter: "{c}"
            },
            yAxisIndex: 0,
            symbol: 'circle',
            symbolSize: 6,
        }, {
            type: 'bar',
            yAxisIndex: 1,
            data: deltavalues,
            itemStyle: {
                borderRadius: 5
            },
            label: {
                show: true,
                position: 'top',
                fontSize: 12,
                color: '#2f4554'
            }
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