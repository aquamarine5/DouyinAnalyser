<!--
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
-->
<script setup>
import { ref } from 'vue';
import LucideClipboardCopy from '~icons/lucide/clipboard-copy?width=18px&height=18px';
import LucideClipboardCheck from '~icons/lucide/clipboard-check?width=18px&height=18px';

const props = defineProps({
    code: {
        type: String,
        required: true
    }
});

function copyToClipboard() {
    navigator.clipboard.writeText(props.code);
    if (copied.value) return;
    copied.value = true;
    setTimeout(() => {
        copied.value = false;
    }, 1000);
}
const copied = ref(false);
</script>

<template>
    <div class="code-block">
        <code>{{ code }}</code>
        <button class="copy-button" @click="copyToClipboard">
            <Transition name="fade" mode="out-in">
                <LucideClipboardCheck v-if="copied" class="copy-icon" />
                <LucideClipboardCopy v-else class="copy-icon" />
            </Transition>
        </button>
    </div>
</template>

<style scoped>
code {
    padding-left: 6px;
}

.code-block {
    display: flex;
    align-items: center;
    background: #f6f8fa;
    justify-content: space-between;
    border-radius: 6px;
    padding: 8px;
    overflow-y: hidden;
    margin: 2px;
    width: 100%;
}

.copy-button {
    justify-self: flex-end;
    padding: 4px 8px;
    background-color: transparent;
    margin-left: 3px;
    transition: background-color 0.1s;
    border: 1px solid #d0d7de;
    border-radius: 4px;
    cursor: pointer;
}

.copy-button:hover {
    transition: background-color 0.1s;
    background-color: #f3f4f6;
}

.copy-icon {
    position: relative;
    font-style: normal;
}

.fade-enter-active {
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-leave-active {
    transition: all 0.15s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-enter-from {
    opacity: 0;
    transform: scale(0.9);
}

.fade-leave-to {
    opacity: 0;
    transform: scale(1.1);
}

.fade-enter-to,
.fade-leave-from {
    opacity: 1;
    transform: scale(1);
}
</style>