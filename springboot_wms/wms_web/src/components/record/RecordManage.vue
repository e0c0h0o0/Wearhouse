<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="Please enter the item name" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="storage" placeholder="Please select a warehouse" style="margin-left: 5px;">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodstype" placeholder="Please select a category" style="margin-left: 5px;">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>

      <el-button type="primary" style="margin-left: 5px;" @click="loadPost">Search</el-button>
      <el-button type="success" @click="resetParam">Reset</el-button>


    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="goodsname" label="Goods Name" width="180">
      </el-table-column>
      <el-table-column prop="storagename" label="Warehouse" width="180">
      </el-table-column>
      <el-table-column prop="goodstypename" label="Goods Type" width="180">
      </el-table-column>
      <el-table-column prop="adminname" label="Operator" width="180">
      </el-table-column>
      <el-table-column prop="username" label="Applicant" width="180">
      </el-table-column>
      <el-table-column prop="count" label="Quantities" width="180">
      </el-table-column>
      <el-table-column prop="createtime" label="Time" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="Note">
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20,30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "RecordManage",
  data() {

    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      storageData:[],
      goodstypeData:[],
      tableData: [],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      storage:'',
      goodstype:'',
      centerDialogVisible:false,
      form:{
        id:'',
        name:'',
        storage:'',
        goodstype:'',
        count:'',
        remark:''
      },
    }
  },
  methods:{
    formatStorage(row){
      let temp =  this.storageData.find(item=>{
        return item.id == row.storage
      })

      return temp && temp.name
    },
    formatGoodstype(row){
      let temp =  this.goodstypeData.find(item=>{
        return item.id == row.goodstype
      })

      return temp && temp.name
    },
    resetForm() {
      this.$refs.form.resetFields();
    },
    handleSizeChange(val) {
      console.log(`${val} items per page`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`Current Page: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.name=''
      this.storage=''
      this.goodstype=''
    },
    loadStorage(){
      this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.storageData=res.data
        }else{
          alert('Fail to get data')
        }

      })
    },
    loadGoodstype(){
      this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.goodstypeData=res.data
        }else{
          alert('Fail to get data')
        }

      })
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/record/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          goodstype:this.goodstype+'',
          storage:this.storage+'',
          roleId:this.user.roleId+'',
          userId:this.user.id+''
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
        }else{
          alert('Fail to get data')
        }

      })
    },
  },
  beforeMount() {
    this.loadStorage()
    this.loadGoodstype()
    this.loadPost()

  }
}
</script>

<style scoped>

</style>