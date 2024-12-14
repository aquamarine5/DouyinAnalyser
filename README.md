# DouyinAnalyser

[![totallines](https://tokei.rs/b1/github/aquamarine5/DouyinAnalyser)](https://github.com/XAMPPRocky/tokei)
[![wakatime](https://wakatime.com/badge/github/aquamarine5/DouyinAnalyser.svg)](https://wakatime.com/badge/github/aquamarine5/DouyinAnalyser)
[![codebeat badge](https://codebeat.co/badges/5e8660c4-55d3-4381-8430-520ca63d3666)](https://codebeat.co/projects/github-com-aquamarine5-douyinanalyser-main)

想知道自己抖音每天点了多少赞？来试试`DouyinAnalyser`吧👉 [dy.aquamarine5.fun](http://dy.aquamarine5.fun)

![my analysis](http://dy.aquamarine5.fun/server/render?id=1)

## Setup

### 配置服务

```bash
gradle build
java -jar build/libs/DouyinAnalyser-${version}.jar
```

这将运行用于链接 MySQL 的 Springboot 服务。  

> [!NOTE]
> 默认将读取环境变量中的 `MYSQL_PASSWORD` 作为MySQL密码。

```bash
cd server ; pnpm i ; npm run server
```

这将配置用于渲染 ECharts 的 Express 服务器。

> [!TIP]
> 我们建议使用 [`pm2`](https://github.com/Unitech/pm2) 进行多程序管理。

### 配置[`Douyin_TikTok_Download_API`](https://github.com/Evil0ctal/Douyin_TikTok_Download_API)

- 根据其README配置完成后，将`port`更改为`1132`即可。

### 配置 MySQL 数据库

```sql
create database dyanalyser;
create table dyanalyser.userinfo
(
    name  text not null,
    `key` text not null,
    id    int auto_increment
        primary key
);
create table dyanalyser.counts
(
    date      int not null,
    userid    int not null,
    likecount int not null,
    primary key (date, userid)
);
```

### 配置前端

- `DouyinAnalyser` 使用 `Vue` 构造前端。

```bash
cd vuefront ; pnpm i
npm run build
```

## HTTP APIs

### for `NodeServer` (port: 1125, [http://dy.aquamarine5.fun/server/](http://dy.aquamarine5.fun/server/) )

- GET `/render?id=${USERID}`  
通过`echarts`渲染点赞分享的SVG图表

- <del>GET `/get?key=${DOUYIN_USERKEY}`  
通过`puppeteer`返回指定用户的当前点赞数</del>  
*已经彻底移除。*

> [!IMPORTANT]
> `puppeteer` 及其依赖已完全从 `DouyinAnalyser` 中移除，由于其在低性能服务器上糟糕的性能表现

### for `Springboot` (port: 1215, [http://dy.aquamarine5.fun/api/](http://dy.aquamarine5.fun/api/) )

- GET `/counter/query?id=${USERID}`  
检索最近14天的点赞数据

- GET `/counter/update`  
强制触发更新全部用户的点赞数据

- POST `/user/add?username=${DOUYIN_USERNAME}&key=${DOUYIN_USERKEY}`  
添加一个新用户以检索点赞数

- GET `/user/list`  
返回所有用户数据

- GET `/user/parse?url=${DOUYIN_SHARE_URL}`  
通过用户首页分享URL获取用户KEY
