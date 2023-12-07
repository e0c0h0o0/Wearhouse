<script setup>
import { ref,defineComponent} from "vue";
import { ElMessageBox, ElMessage } from 'element-plus';

const user = ref({ name: null });

try {
  const storedUser = sessionStorage.getItem('CurUser');
  if (storedUser) {
    try {
      // 将 user.value 设置为解析后的 JSON 数据
      user.value = JSON.parse(storedUser).name;
      console.log('User found:', user.value);
    } catch (parseError) {
      console.error('Error parsing stored user data:', parseError);
      // 处理无效的 JSON 格式
    }
  } else {
    console.log('No user in sessionStorage');
    // 处理缺少用户数据的情况
  }
} catch (error) {
  console.error('Error retrieving user data from sessionStorage:', error);
}

function toUser() {
  console.log("User");
  router.push("/Home");
}

function logOut() {
  console.log("logout");
  ElMessageBox.confirm('Confirmation of logging out?', 'Warning', {
    confirmButtonText: 'OK',
    type: 'warning',
    center: true,
  })
      .then(() => {
        ElMessage({
          type: 'success',
          message: 'Exit Login Successful'
        });
        router.push("/");
        sessionStorage.clear();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: 'Canceled'
        });
      });
}


const emit = defineEmits(['doCollapse']);

function collapse() {
  emit('doCollapse');
}

const props = defineProps({
  icon: {
    type: Object,
    default: () => Fold, // Default icon if none is passed
  },
});
function created(){
  return router.push('/Home')
}

import { ArrowDown, Fold } from "@element-plus/icons-vue";
import router from "@/router";
</script>

<template>
  <div style="display: flex;line-height: 60px" class="toolbar">
    <div style="margin-top: 8px">
      <el-icon style="font-size: 20px;cursor: pointer" @click="collapse"><component :is="icon" /></el-icon>
    </div>
    <div style="flex:1;text-align:center;font-size: 30px">
      <span>Welcome to Warehouse Management System</span>
    </div>
    <span v-if="user">{{ user }}</span>
    <span v-else>User not available</span>
    <el-dropdown>
      <el-icon style="margin-left: 5px;margin-right: 8px; margin-top: 1px"><ArrowDown /></el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="toUser">Personal Center</el-dropdown-item>
          <el-dropdown-item @click="logOut">Logout</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<style scoped>

</style>