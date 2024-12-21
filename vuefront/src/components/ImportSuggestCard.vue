<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup lang="js">
import { useRouter } from 'vue-router';
import LineMdPersonAddTwotone from '~icons/line-md/person-add-twotone?width=24px&height=24px';
const router = useRouter();
function click() {
    router.push({ name: "import" })
}
</script>

<template>
    <div class="importsuggest_bg">
        <LineMdPersonAddTwotone class="importsuggest_icon" />
        <div class="importsuggest_container" @click="click">
            <div class="importsuggest_title">
                还没有导入用户？
            </div>
            <div class="importsuggest_text">
                点击跳转至导入页面，只需要一分钟就好啦😉
            </div>
        </div>
    </div>
</template>

<style>
.importsuggest_icon {
    padding-right: 10px;
    padding-left: 7px;
    color: white;
    min-height: 28px;
    min-width: 28px;
}

.importsuggest_bg {
    cursor: pointer;
    background-color: #04d4fd;
    display: flex;
    align-items: center;
    padding: 10px 7px;
    border-radius: 10px;
    font-weight: 400;

}

.importsuggest_title {
    color: white;
    font-size: medium;
    font-weight: 600;
}

.importsuggest_text {
    color: white;
    font-size: smaller;
    font-weight: 500;
}
</style>