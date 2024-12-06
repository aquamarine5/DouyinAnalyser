@echo off
setlocal

REM 设置变量
set PASSWORD=140309417aqua@
set USER=root
set HOST=47.93.43.91
sshpass -p %PASSWORD% ssh %USER%@%HOST% "cd /root/DouyinAnalyser/; git pull; cd server; pm2 start --name DouyinAnalyserNodeServer server.js"