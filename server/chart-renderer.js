/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */
import * as echarts from 'echarts/core';
import { SVGRenderer } from 'echarts/renderers';
import { BarChart, LineChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';

export function renderChart(data) {
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

    const chart = echarts.init(null, null, {
        renderer: 'svg',
        ssr: true,
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
    const avgDeltaValues = deltavalues.reduce((a, b) => a + b) / deltavalues.length;
    deltavalues.push(avgDeltaValues);
    chart.setOption({
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
            bottom: 30,
            show: false
        },
        graphic: [{
            type: 'rect',
            shape: {
                x: 0,
                y: 0,
                width: 450,
                height: 300,
                r: 8
            },
            style: {
                fill: '#ffffff'
            },
            z: -1
        }],
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
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
            min: Math.floor(deltaMinValues * 0.6),
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
                formatter: (params) => {
                    console.log(params)
                    return params.dataIndex === values.length - 1 ?
                        params.value :
                        '';
                }
            }
        }, {
            type: 'bar',
            yAxisIndex: 1,
            data: deltavalues,
            itemStyle: {
                borderRadius: [8, 8, 2, 2],
                color: (params) => {
                    return params.dataIndex === deltavalues.length - 1 ?
                        '#FBA414'
                        : '#67C23A'
                }
            },
            label: {
                show: true,
                position: 'top',
                fontSize: 10,
                color: '#2f4554',
                formatter: (params) => {
                    return params.dataIndex === deltavalues.length - 1 ?
                        `平均\n${params.value.toFixed(1)}` :
                        params.value;
                }
            }
        }],
        animation: true,
    });

    const svgStr = chart.renderToSVGString();

    chart.dispose();

    return svgStr;
}