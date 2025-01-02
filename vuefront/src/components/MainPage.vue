<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup>
import TitleBar from '@/TitleBar.vue';
import "@/css/general.css"
import axios from 'axios';
import { ElInput, ElSelect, ElOption } from 'element-plus';
import { ref } from 'vue';
import LineMdPersonSearchTwotone from '~icons/line-md/person-search-twotone?width=24px&height=24px';
import IntroductionCard from './IntroductionCard.vue';
import LineMdAlertLoop from '~icons/line-md/alert-loop?width=24px&height=24px';
import SponsorCard from './SponsorCard.vue';
import CodePaste from './CodePaste.vue'
import ImportSuggestCard from './ImportSuggestCard.vue';
const inputIDValue = ref();
const imgsrcValue = ref("");
const imgPasteValue = ref("");
const isPermission = ref(false)
const allUserIds = ref([]);

function setImgSrcValue() {
    imgsrcValue.value = `http://dy.aquamarine5.fun/server/render?id=${inputIDValue.value}&no-cache`;
    imgPasteValue.value = `http://dy.aquamarine5.fun/server/render?id=${inputIDValue.value}`;
}

function handleSelect(value) {
    inputIDValue.value = value;
    setImgSrcValue();
}

axios.get("http://dy.aquamarine5.fun/api/user/list").then(response => {
    console.log(response);
    allUserIds.value = response.data.data.list;
    isPermission.value = response.data.data.permission
});
</script>

<template>
    <TitleBar>
        DouyinAnalyser
    </TitleBar>
    <div class="page_container">
        Not Final Version.
        <IntroductionCard />
        <div class="selecting_container">
            <LineMdPersonSearchTwotone class="selecting_icon" />
            <div>
                <div class="selecting_title">
                    选择查看的用户：
                </div>
                <div class="selecting_inputvalue">
                    <ElSelect v-model="inputIDValue" placeholder="选择用户" @change="handleSelect"
                        class="selecting_username">
                        <ElOption v-for="user in allUserIds" :key="user.id" :label="user.name" :value="user.id" />
                    </ElSelect>
                    或
                    <ElInput placeholder="输入AnalyserID" v-model="inputIDValue" @change="handleSelect" />
                </div>
            </div>
        </div>
        <ImportSuggestCard />
        <SponsorCard />
        <div class="img_container" v-if="inputIDValue">
            <img :src="imgsrcValue" class="img" />
            <div class="permission_warn_div" v-if="isPermission">
                <LineMdAlertLoop class="permission_warn_icon" />
                请注意！在最近一次的数据更新内，该用户的喜欢列表设置为了私密！
            </div>
            <CodePaste :code="imgPasteValue" />
        </div>
    </div>
</template>

<style scoped>
.permission_warn_div {
    display: flex;
    gap: 6px;
    align-items: center;
    width: 100%;
    padding: 8px;
    margin: 2px;
    border-radius: 10px;
    background-color: yellow;
    font-size: small;
}

.permission_warn_icon {
    min-height: 24px;
    min-width: 24px;
    padding-left: 3px;
}

.selecting_icon {
    min-height: 28px;
    min-width: 28px;
}

.selecting_title {
    font-size: medium;
    font-weight: 600;
    padding-bottom: 6px;
    padding-top: 3px;
    padding-left: 1px;
}

.selecting_inputvalue {
    display: flex;
    gap: 10px;
    align-items: center;
}

.selecting_username {
    min-width: 150px;
}

.img_container {
    flex-direction: column;
    width: 100%;
    gap: 5px;
    display: flex;
    border-width: 5px;
    border-style: solid;
    align-items: center;
    border-color: transparent;
    background-image: linear-gradient(to right, #fff, #fff), linear-gradient(135deg, #A9C9FF, #FFBBEC);
    background-clip: padding-box, border-box;
    background-origin: padding-box, border-box;
    border-radius: 10px;
    padding: 8px 12px;
    box-sizing: border-box;
}

.page_container {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.selecting_container {
    gap: 7px;
    background-color: #BDBDBD;
    display: flex;
    align-items: center;
    padding: 7px 7px 12px 14px;
    border-radius: 10px 10px;
}

.img {
    width: 100%;
}
</style>