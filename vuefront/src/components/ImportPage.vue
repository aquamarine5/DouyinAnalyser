<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup>

import TitleBar from '@/TitleBar.vue';
import "@/css/general.css"
import axios from 'axios';
import { ElInput, ElNotification } from 'element-plus';
import { ref } from 'vue';
const inputSharedTokenValue = ref("");
const inputUsernameValue = ref("");
const output = ref("");
const userkey = ref("");

function addUser() {
    console.log("Adding user...");
    console.log("SharedToken: " + inputSharedTokenValue.value);
    console.log("Username: " + inputUsernameValue.value);
    if (userkey.value == "") {
        ElNotification({
            title: "错误",
            message: "请先获取Key",
            type: "error"
        });
        return
    }
    fetch(`http://dy.aquamarine5.fun/api/user/add?username=${inputSharedTokenValue.value}&key=${userkey.value}`, {
        method: "POST"
    }).then(response => response.json())
        .then(data => {
            console.log(data);
            output.value = `你的id是${data.data.userid}`;
        });
}
function parseKey() {
    function returnUrl() {

        const regex = /https:\/\/v\.douyin\.com\/[a-zA-Z0-9]+\//;
        const match = inputSharedTokenValue.value.match(regex);
        return match ? match[0] : null;
    }
    const url = returnUrl();
    if (!url) {
        ElNotification({
            title: "错误",
            message: "无法解析分享口令",
            type: "error"
        });
        return
    }
    axios.get(`http://dy.aquamarine5.fun/api/user/parse?url=${url}`).then(response => {
        console.log(response);
        userkey.value = response.data.key;
    });
}
</script>

<template>
    <TitleBar>
        DouyinAnalyser: 导入
    </TitleBar>
    <div class="page_container">
        Not Final Version.
        <img class="readme_img" src="http://dy.aquamarine5.fun/img/import_readme.jpg" />
        <div class="notice">
            请注意！使用本功能必须把喜欢列表设为公开！
        </div>
        <ElInput placeholder="请输入分享口令" v-model="inputSharedTokenValue" />
        <ElButton type="primary" @click="parseKey">第一步：获取Key</ElButton>
        <ElInput placeholder="请输入抖音昵称" v-model="inputUsernameValue" />
        <div>
            Userkey: {{ userkey }}
        </div>
        <ElButton type="primary" @click="addUser">添加</ElButton>
        <div>
            {{ output }}
        </div>
    </div>
</template>

<style scoped>
.readme_img {
    max-width: 90vw;
    max-height: 90vh;
}
</style>