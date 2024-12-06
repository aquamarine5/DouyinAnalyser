import * as echarts from 'echarts/core';
import { SVGRenderer } from 'echarts/renderers';
import { BarChart } from 'echarts/charts';
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

    // 注册必要的组件
    echarts.use([
        SVGRenderer,
        BarChart,
        TitleComponent,
        TooltipComponent,
        LegendComponent,
        GridComponent
    ]);

    // 初始化图表实例
    const chart = echarts.init(div, null, {
        renderer: 'svg',
        width: 600,
        height: 400
    });
    categories = []
    for (let i = 0; i < data.length; i++) {
        categories.push(data[i].date)
    }
    values = []
    for (let i = 0; i < data.length; i++) {
        values.push(data[i].likecount)
    }
    // 配置图表选项
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
            type: 'value'
        },
        series: [{
            type: 'line',
            data: values
        }],
        animation: true
    };

    // 设置选项并渲染
    chart.setOption(option);

    // 获取 SVG 字符串
    const svgStr = div.querySelector('svg').outerHTML;


    chart.dispose();
    delete global.window;
    delete global.document;

    return svgStr;
}