# DouyinAnalyser

[![totallines](https://tokei.rs/b1/github/aquamarine5/DouyinAnalyser)](https://github.com/XAMPPRocky/tokei)
[![wakatime](https://wakatime.com/badge/github/aquamarine5/DouyinAnalyser.svg)](https://wakatime.com/badge/github/aquamarine5/DouyinAnalyser)
[![codebeat badge](https://codebeat.co/badges/5e8660c4-55d3-4381-8430-520ca63d3666)](https://codebeat.co/projects/github-com-aquamarine5-douyinanalyser-main)

想知道自己抖音每天点了多少赞？来试试`DouyinAnalyser`吧👉 [dy.aquamarine5.fun](http://dy.aquamarine5.fun)

![my analysis](http://dy.aquamarine5.fun/server/render?id=1)

## HTTP APIs

### for `NodeServer` (port: 1125, [http://dy.aquamarine5.fun/server/](http://dy.aquamarine5.fun/server/) )

- GET `/get?key=${DOUYIN_USERKEY}`  
通过`puppeteer`返回指定用户的当前点赞数

- GET `/render?id=${USERID}`  
通过`echarts`渲染点赞分享的SVG图表

### for `Springboot` (port: 1215, [http://dy.aquamarine5.fun/api/](http://dy.aquamarine5.fun/api/) )

- GET `/counter/query?id={USERID}`  
检索最近14天的点赞数据

- GET `/counter/update`  
强制触发更新全部用户的点赞数据

- POST `/user/add?username=${DOUYIN_USERNAME}&key=${DOUYIN_USERKEY}`  
添加一个新用户以检索点赞数
