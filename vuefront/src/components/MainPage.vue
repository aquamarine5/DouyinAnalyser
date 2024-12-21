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
import SponsorCard from './SponsorCard.vue';
import CodePaste from './CodePaste.vue'
import ImportSuggestCard from './ImportSuggestCard.vue';
const inputIDValue = ref();
const imgsrcValue = ref("");
const imgPasteValue = ref("");

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
                <ElSelect v-model="inputIDValue" placeholder="选择用户" @change="handleSelect">
                    <ElOption v-for="user in allUserIds" :key="user.id" :label="user.name" :value="user.id" />
                </ElSelect>
                <ElInput placeholder="输入AnalyserID" v-model="inputIDValue" />
            </div>
        </div>
        <ImportSuggestCard />
        <SponsorCard />
        <div class="img_container" v-if="inputIDValue">
            <img :src="imgsrcValue" class="img" />
            <CodePaste :code="imgPasteValue" />
        </div>
    </div>
</template>

<style scoped>
.selecting_icon {
    min-height: 28px;
    min-width: 28px;
}

.img_container {
    flex-direction: column;
    width: 100%;
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