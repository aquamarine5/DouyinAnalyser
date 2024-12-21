<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup>
import LineMdTiktok from '~icons/line-md/tiktok?width=32px&height=32px';
import LineMdGithubTwotone from '~icons/line-md/github-twotone?width=32px&height=32px';
import pkginfo from '../package.json';
import '@/assets/fonts/font.css';
import { useRouter } from 'vue-router';
const router = useRouter()
function gotoHome() {
    router.push({
        name: "mainpage"
    })
}
function jumpToGithub() {
    window.open("https://github.com/aquamarine5/DouyinAnalyser", "_blanks")
}

function jumpToDouyin() {
    window.open("https://www.douyin.com/user/MS4wLjABAAAApuyqymIaQkpvKkbdH1X6W3A6XEgJl7kddGrZHxipJ7TbA1lCRaPJK5gZ1KX7pR1n", "_blanks")
}
</script>

<template>
    <div>
        <div class="app_topbar">
            <div class="app_title" @click="gotoHome">
                <slot></slot>
                <div class="topbar_version">
                    {{ pkginfo.version }}
                </div>
            </div>

            <LineMdTiktok class="topbar_douyin" @click="jumpToDouyin" />
            <LineMdGithubTwotone class="topbar_github" @click="jumpToGithub" />
        </div>
    </div>
</template>

<style>
.topbar_version {
    font-size: small;
    padding-left: 2px;
}

.app_topbar {
    background-color: blueviolet;
    display: flex;
    align-items: center;
    border-radius: 0 0 10px 10px;
}

.app_title {
    font-family: Gilroy;
    cursor: pointer;
    display: flex;
    white-space: nowrap;
    padding-top: 15px;
    padding-inline: 20px;
    height: 40px;
    font-size: larger;
    color: #f5f5f5;
    font-weight: 800;
}

.topbar_github {
    cursor: pointer;
    justify-self: end;
    margin-left: 9px;
    margin-right: 18px;
}

.topbar_douyin {
    cursor: pointer;
    justify-self: end;
    margin-left: auto;
}
</style>