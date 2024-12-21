<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup lang="js">
import LineMdEmojiSmileWinkTwotone from '~icons/line-md/emoji-smile-wink-twotone?width=28px&height=28px';
</script>

<template>
    <div class="introduction_bg">
        <LineMdEmojiSmileWinkTwotone class="introduction_icon" />
        <div class="introduction_container">
            <div class="introduction_title">
                这是什么？
            </div>
            <div class="introduction_text">
                DouyinAnalyser 可以监控登记过的抖音用户每天的点赞数，并总结出分析表格。
            </div>
            <div class="introduction_text" style="font-style: oblique; text-decoration: underline;">
                本工具还在持续更新中，呈现界面并非最终版本。
            </div>
        </div>
    </div>
</template>

<style>
.introduction_icon {
    padding-right: 8px;
    padding-left: 7px;
    color: white;
    min-height: 28px;
    min-width: 28px;
}

.introduction_bg {
    background-color: #ffd000;
    display: flex;
    align-items: center;
    padding: 10px 7px;
    border-radius: 10px;
    font-weight: 400;

}

.introduction_title {
    color: white;
    font-size: medium;
    font-weight: 600;
}

.introduction_text {
    color: white;
    font-size: smaller;
    font-weight: 500;
}
</style>