import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router";
import store from "@/store";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/global.css'
import axios from 'axios'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
const app = createApp(App)
// 全局注册
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.config.globalProperties.$axios = axios
app.config.globalProperties.$httpUrl = 'http://localhost:8080';
app.use(router);
app.use(store);
app.use(ElementPlus,{size:'small'})

app.mount('#app')
