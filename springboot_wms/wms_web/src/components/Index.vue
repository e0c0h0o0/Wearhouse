<template>
  <el-container class="layout-container-demo" style="height: 100%">
    <el-aside :width="aside_width" style=" height: 100%">
      <Aside :isCollapse="isCollapse"/>
    </el-aside>

    <el-container style="height: 100%">
      <el-header style="text-align: right; margin-left:-1px ;font-size: 12px;border-bottom: darkgray 1px solid">
        <Header @doCollapse="doCollapse" :icon="icon"/>
      </el-header>

      <el-main style="height: 100%">
<!--        <Main/>-->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>


import {markRaw, ref} from 'vue'

import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

import { Fold, Expand } from '@element-plus/icons-vue';
const isCollapse = ref(false);
const aside_width = ref("200px");
const icon = ref(markRaw(Expand));

function doCollapse(){
  isCollapse.value=!isCollapse.value;
  if(!isCollapse.value){
    aside_width.value="200px";
    icon.value = markRaw(Fold);
    }else{
    aside_width.value="64px";
    icon.value =markRaw(Expand);
    }
}
</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  background-color: cadetblue;
  color: var(--el-text-color-primary);
}

.el-main {
  padding: 5px;
}

.layout-container-demo .el-aside {
  height: 100%;
  color: var(--el-text-color-primary);
  background: var(--el-color-primary-light-8);
}

.layout-container-demo .el-menu {
  border-right: none;
}

.layout-container-demo .el-main {
  height: 100%;
  padding: 5px;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>