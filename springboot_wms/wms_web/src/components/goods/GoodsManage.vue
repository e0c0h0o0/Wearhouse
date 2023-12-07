<template>
  <div>
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="Please enter goods name" suffix-icon="el-icon-search" style="width: 200px;"
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

      <el-button type="primary" style="margin-left: 5px;" @click="add" v-if="user.roleId!=2">Add</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="inGoods" v-if="user.roleId!=2">Inbound</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="outGoods" v-if="user.roleId!=2">Outbound</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="Goods Name" width="180">
      </el-table-column>
      <el-table-column prop="storage" label="Warehouse" width="180" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="Categorization" width="180" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="Number" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="Note">
      </el-table-column>
      <el-table-column  label="操作" v-if="user.roleId!=2">
          <template #default="scope">
            <el-button size="small" type="success" @click="mod(scope.row)">Edit</el-button>
            <el-popconfirm title="Are you sure about the deletion?" @confirm="del(scope.row.id)">
              <template #reference>
                <el-button slot="reference" size="small" type="danger" >Delete</el-button>
              </template>
            </el-popconfirm>
          </template>
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

    <el-dialog
        title="物品维护"
        v-model="centerDialogVisible"
        width="30%"
        center>

      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="goodsname" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="warehouse" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" placeholder="Please choose a Warehouse" style="margin-left: 5px;">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>

          </el-col>
        </el-form-item>
        <el-form-item label="categories" prop="goodstype">
          <el-col :span="20">
            <el-select v-model="form.goodstype" placeholder="Please select a category" style="margin-left: 5px;">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="number" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="note" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="save">Confirm</el-button>
  </span>
    </el-dialog>

    <el-dialog
        title="出入库"
        v-model="inDialogVisible"
        width="30%"
        center>

      <el-dialog
          width="75%"
          title="用户选择"
          v-model="innerVisible"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
                    <el-button @click="innerVisible = false">Cancel</el-button>
                    <el-button type="primary" @click="confirmUser">Confirm</el-button>
                  </span>
      </el-dialog>

      <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">
        <el-form-item label="物品名">
          <el-col :span="20">
            <el-input v-model="form1.goodsname" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人">
          <el-col :span="20">
            <el-input v-model="form1.username" readonly @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form1.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form1.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="doInGoods">Confirm</el-button>
  </span>
    </el-dialog>
  </div>
</template>


<script>
import SelectUser from "../user/SelectUser.vue";
export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule, value, callback) => {
      if(value>9999){
        callback(new Error('Invalid number'));
      }else{
        callback();
      }
    };

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
      inDialogVisible:false,
      innerVisible:false,
      currentRow:{},
      tempUser:{},
      form:{
        id:'',
        name:'',
        storage:'',
        goodstype:'',
        count:'',
        remark:''
      },
      form1:{
        goods:'',
        goodsname:'',
        count:'',
        username:'',
        userid:'',
        adminId:'',
        remark:'',
        action:'1'
      },
      rules1: {

      },
      rules: {
        name: [
          {required: true, message: 'Please enter a goods name', trigger: 'blur'}
        ],
        storage:[
          {required: true, message: 'Please choose a warehouse', trigger: 'blur'}
        ],
        goodstype:[
          {required: true, message: 'Please choose a category', trigger: 'blur'}
        ],
        count: [
          {required: true, message: 'Please enter a number', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: 'Invalid number',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    doSelectUser(val){
      console.log(val)
      this.tempUser = val
    },
    confirmUser(){
      this.form1.username = this.tempUser.name
      this.form1.userid = this.tempUser.id

      this.innerVisible = false
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
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
      this.form={
        id:'',
        name:'',
        storage:'',
        goodstype:'',
        count:'',
        remark:''
      }
    },
    resetInForm(){
      this.form1={
        goods:'',
        goodsname:'',
        count:'',
        username:'',
        userid:'',
        adminId:'',
        remark:'',
        action:'1'
      }
    },
    del(id){
      console.log(id)

      this.$axios.get(this.$httpUrl+'/goods/del?id='+id).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: 'Success！',
            type: 'success'
          });
          this.loadPost()
        }else{
          this.$message({
            message: 'Fail！',
            type: 'error'
          });
        }

      })
    },
    mod(row){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        //赋值到表单
        this.form.id = row.id
        this.form.name = row.name
        this.form.storage = row.storage
        this.form.goodstype = row.goodstype
        this.form.count = row.count
        this.form.remark = row.remark
      })
    },
    add(){

      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm()

        this.form.id = ''
      })

    },
    inGoods(){
      console.log(this.currentRow)
      if(!this.currentRow.id){
        alert('Please select a record');
        return;
      }
      this.inDialogVisible = true

      this.form1.goodsname = this.currentRow.name
      this.form1.goods = this.currentRow.id
      this.form1.adminId=this.user.id
      this.form1.action='1'
    },
    outGoods(){
      if(!this.currentRow.id){
        alert('Please select a record');
        return;
      }
      this.inDialogVisible = true
      this.form1.goodsname = this.currentRow.name
      this.form1.goods = this.currentRow.id
      this.form1.adminId=this.user.id
      this.form1.action='2'

    },
    selectUser(){
      this.innerVisible=true
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: 'Success！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this. resetForm()
        }else{
          this.$message({
            message: 'Fail！',
            type: 'error'
          });
        }

      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: 'Success！',
            type: 'success'
          });

          this.centerDialogVisible = false
          this.loadPost()
          this. resetForm()
        }else{
          this.$message({
            message: 'Fail！',
            type: 'error'
          });
        }

      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    doInGoods(){
      this.$axios.post(this.$httpUrl+'/record/save',this.form1).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: 'Success！',
            type: 'success'
          });
          this.inDialogVisible = false
          this.loadPost()
          this. resetInForm()
        }else{
          this.$message({
            message: 'Fail！',
            type: 'error'
          });
        }

      })
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
    loadPost(){
      this.$axios.post(this.$httpUrl+'/goods/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          goodstype:this.goodstype+'',
          storage:this.storage+''
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
    }
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
