<template>
  <div style="text-align: center;background-color: #f1f1f3;height: 100%;padding: 0;margin: 0;">
    <h1 style="font-size: 50px;">{{'Welcome '+user.name}}</h1>
    <el-descriptions  title="Personal Central" :column="2" size="medium" border>
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-s-custom"></i>
            Account
        </template>
        {{user.no}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-mobile-phone"></i>
          Phone Number
        </template>
        {{user.phone}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-location-outline"></i>
          Gender
        </template>
        <el-tag
            :type="user.sex === '1' ? 'primary' : 'danger'"
            disable-transitions><i :class="user.sex==='1'?'el-icon-male':'el-icon-female'"></i>{{user.sex==='1'?"Male":"Female"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-tickets"></i>
          Role
        </template>
        <el-tag
            type="success"
            disable-transitions>{{user.roleId==0?"Supervisor":(user.roleId==1?"Manger":"User")}}</el-tag>

      </el-descriptions-item>
    </el-descriptions>

    <DateUtils></DateUtils>
  </div>
</template>

<script>
import DateUtils from "./DateUtils.vue";
export default {
  name: "Home",
  components: {DateUtils},
  data() {

    return {
      user:{}
    }
  },
  computed:{

  },
  methods:{
    init(){
      this.user = JSON.parse(sessionStorage.getItem('CurUser'))
      console.log('user', this.user)
    }
  },
  created(){
    this.init()
  }
}
</script>

<style scoped>
.el-descriptions{
  width:90%;

  margin: 0 auto;
  text-align: center;
}
</style>