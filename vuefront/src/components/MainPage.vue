<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup>
import TitleBar from '@/TitleBar.vue';
import "@/css/general.css"
import axios from 'axios';
import { ElButton, ElInput, ElSelect, ElOption } from 'element-plus';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const inputIDValue = ref();
const imgsrcValue = ref("");
const router = useRouter();
const allUserIds = ref([]);

function setImgSrcValue() {
    imgsrcValue.value = `http://dy.aquamarine5.fun/server/render?id=${inputIDValue.value}`;
}

function handleSelect(value) {
    inputIDValue.value = value;
    setImgSrcValue();
}

axios.get("http://dy.aquamarine5.fun/api/user/list").then(response => {
    console.log(response);
    allUserIds.value = response.data.data.list;
});
</script>

<template>
    <TitleBar>
        DouyinAnalyser
    </TitleBar>
    <div class="page_container">
        Not Final Version.
        <ElSelect v-model="inputIDValue" placeholder="选择用户" @change="handleSelect">
            <ElOption v-for="user in allUserIds" :key="user.id" :label="user.name" :value="user.id" />
        </ElSelect>
        <ElInput placeholder="输入AnalyserID" v-model="inputIDValue" />
        <ElButton type="primary" @click="setImgSrcValue">查询</ElButton>
        <ElButton type="primary" @click="router.push({ name: 'import' })">没有用户？点击添加。</ElButton>
        <img :src="imgsrcValue" />
    </div>
</template>