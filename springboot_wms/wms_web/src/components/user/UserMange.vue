<script setup>
import {onBeforeMount, ref, getCurrentInstance,nextTick,reactive} from "vue";
import axios from 'axios';
import {Search} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
const tableData = ref([]);
const loading = ref(false);

const centerDialogVisible = ref(false);
const formRef = ref(null);

const pageSize = ref(5);
const pageNum = ref(1);
const total = ref(0);
const name = ref('');
const sex = ref('');
const gender = ref([
  {
    value: '1',
    label: 'Male',
  },
  {
    value: '0',
    label: 'Female',
  }
]);
const form = ref({
  id:'',
  no: '',
  name: '',
  password: '',
  age: '',
  phone: '',
  sex: '0',
  roleId:'2'
})
const checkDuplicate = async (rule, value, callback) => {
  // 检查form.value.id而不是this.form.id
  if (form.value.id) {
    return callback();
  }
  const response = await axios.get(httpUrl+'/user/findByNo', {
    params: { no: form.value.no }
  });
  if(response.data.code!==200){
    callback();
  }else {
    callback(new Error("Account already exists"));
  }
};
let checkAge = (rule, value, callback) => {
  if(value>150){
    callback(new Error('Invalid number')); }
  else{
    callback();
  }
};
const rules = reactive({
  no: [
    {required: true, message: 'Please input Account no', trigger: 'blur'},
    {min: 3, max: 8, message: 'Length should be 3 to 8', trigger: 'blur'},
    { validator: checkDuplicate, trigger: 'blur' },
  ],
  name: [
    {required: true, message: 'Please input name', trigger: 'blur'}
  ],
  password: [
    {required: true, message: 'Please input password', trigger: 'blur'},
    {min: 3, max: 8, message: 'Length should be 3 to 8', trigger: 'blur'},
  ],
  age: [
    {required: true, message: "Please input age", trigger: 'blur'},
    {min: 1, max: 3, message: 'The degree of separation is between 1 and 3 digits.', trigger: 'blur'},
    {pattern: /^[1-9][0-9]{0,2}$/,message: 'Age must be a positive integer',trigger: "blur"},
    {validator:checkAge,trigger: 'blur'}
  ],
  phone: [
    {required: true,message: "Cell phone number cannot be empty",trigger: "blur"},
    {pattern: /^1[3456789]\d{9}$/, message: "Please enter the correct cell phone number", trigger: "blur"}]
})

const instance = getCurrentInstance();
const httpUrl = instance.appContext.config.globalProperties.$httpUrl

const loadGet = () => {
  loading.value = true;
  // 直接调用axios.get
  axios.get(httpUrl + '/user/list')
      .then(response => {
        tableData.value = response.data;
      })
      .catch(error => {
        // 处理错误情况
        console.error("Error fetching data: ", error);
      })
      .finally(() => {
        loading.value = false; // 加载结束，设置加载状态为false
      });
};
const loadPost = () => {
  loading.value = true;
  axios.post(httpUrl + '/user/listPageC1 ', {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    param: {
      name: name.value,
      sex: sex.value,
      roleId:'2'
    }
  })
      .then(response => {
        console.log(response.data);
        if (response.data.code === 200) {
          tableData.value = response.data.data;
          total.value = response.data.total;
        } else {
          console.error("Error posting data: ", response.data.message);
        }
      })
      .catch(error => {
        console.error("Unexpected response code or missing data", response.data.error()); // Handle error scenario
      })
      .finally(() => {
        // 完成请求（无论成功或失败），设置加载状态为false
        loading.value = false;
      });
}
onBeforeMount(() => {
  //loadGet();
  loadPost();
});
const handleSizeChange = (val) => {
  console.log(`${val} items per page`)
  pageNum.value = 1;
  pageSize.value = val;
  loadPost();
}
const handleCurrentChange = (val) => {
  console.log(`current page: ${val}`)
  pageNum.value = val;
  loadPost();
}
const resetSearch = () => {
  name.value = '';
  sex.value = '';
};
const resetForm = () => {
  if (formRef.value) { // 确保 formRef 不是 null
    formRef.value.resetFields();
  }
};
const del=(id)=>{
  console.log(id)
  axios.get(httpUrl + '/user/del?id='+id)
      .then(response => {
        console.log(response.data);
        if (response.data.code === 200) {
          open2();
          loadPost();
        } else {
          open4();
        }
      })

}
const mod= (row)=>{
  console.log(row)
  centerDialogVisible.value = true;
  nextTick(()=>{
    form.value.id = row.id;
    form.value.no = row.no;
    form.value.name = row.name;
    form.value.password = '';
    form.value.age= row.age+'';
    form.value.sex = row.sex+'';
    form.value.roleId = row.roleId;
    form.value.phone = row.phone;
  })
}
const add = () => {
  centerDialogVisible.value = true;
  nextTick(() => {
    resetForm();
  });
};
const open2 = () => {
  ElMessage({
    message: 'Operation Completed.',
    type: 'success',
  })
}

const open4 = () => {
  ElMessage.error('Oops, this is a error message.')
}
const doSave=()=>{
  axios.post(httpUrl + '/user/save  ',form.value)
      .then(response => {
        console.log(response.data);
        if (response.data.code === 200) {
          open2();
          centerDialogVisible.value = false;
          loadPost();
          resetForm();
        } else {
          open4();
        }
      })

}
const doMod=()=>{
  axios.post(httpUrl + '/user/update',form.value)
      .then(response => {
        console.log(response.data);
        if (response.data.code === 200) {
          open2();
          centerDialogVisible.value = false;
          loadPost();
          resetForm();
        } else {
          open4();
        }
      })

}
const submitForm = async () => {
  if (!formRef.value) return; // 如果 formRef 是 null 或者 undefined，就直接返回。
  await formRef.value.validate((valid, fields) => {
    if (valid) {
      if(form.value.id){
        doMod();
      }else{
        doSave();
      }
    } else {
      console.log('error submit!', fields);
    }
  });
};
const save =() =>{
  submitForm();
}

</script>

<template>
  <el-scrollbar>
    <div style="margin-bottom: 5px">

      <el-input placeholder="Please enter a name" v-model="name" style="width: 200px"
                @keyup.enter.native="loadPost">
        <template #suffix>
          <el-icon>
            <Search/>
          </el-icon>
        </template>
      </el-input>

      <el-select v-model="sex" filterable placeholder="Please select gender" style="margin-left: 5px">
        <el-option
            v-for="item in gender"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">Search</el-button>
      <el-button type="success" @click="resetSearch">Reset</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add ">Add</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{ backgroundColor: '#f2f5fc',color:'#555555'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60"/>
      <el-table-column prop="no" label="Account" width="180"/>
      <el-table-column prop="name" label="Name" width="180"/>
      <el-table-column prop="age" label="Age" width="80"/>
      <el-table-column prop="sex" label="Sex" width="90">
        <template #default="scope">
          <el-tag
              :type="scope.row.sex == 1? 'success' : ''"
              disable-transitions>
            {{ scope.row.sex == 1 ? 'Male' : 'Female' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleId" label="RoleId" width="110">
        <template #default="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (
                scope.row.roleId === 1 ? 'primary':'success'
              )"
              disable-transitions>
            {{
              scope.row.roleId === 0 ? 'Supervisor' : (
                  scope.row.roleId === 1 ? 'Manager' : 'Customer'
              )
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="PhoneNumber" width="120"/>
      <el-table-column prop="operate" label="Operating Area">
        <template #default="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">Edit</el-button>
          <el-popconfirm title="Are you sure to delete this?"
                         @confirm="del(scope.row.id)">
            <template #reference>
              <el-button  size="small" type="danger" >Delete</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20,30]"
        :small="small"
        :disabled="disabled"
        :background="background"
        layout="sizes, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 5px"
    />
    <el-dialog v-model="centerDialogVisible" title="Add Information" width="30%"  >
      <el-form
          ref="formRef"
          :rules="rules"
          :model="form"
          label-width="80px"
          label-position="left"
      >
        <el-form-item label="Account" prop="no">
          <!--          <el-col :span="20">-->
          <el-input v-model="form.no" />
          <!--          </el-col>-->
        </el-form-item>

        <el-form-item label="Name" prop="name">
          <!--          <el-col :span="20">-->
          <el-input v-model="form.name"/>
          <!--          </el-col>-->
        </el-form-item>

        <el-form-item label="Password" prop="password">
          <!--        <el-col :span="20">-->
          <el-input v-model="form.password"/>
          <!--        </el-col>-->
        </el-form-item>

        <el-form-item label="Age" prop="age">
          <!--        <el-col :span="20">-->
          <el-input v-model="form.age"/>
          <!--        </el-col>-->
        </el-form-item>

        <el-form-item label="Gender">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">Male</el-radio>
            <el-radio label="0">Female</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="PhoneNum" prop="phone">
          <!--        <el-col :span="20">-->
          <el-input v-model="form.phone"/>
          <!--        </el-col>-->
        </el-form-item>

      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
      </template>
    </el-dialog>
  </el-scrollbar>
</template>

<style scoped>

.el-dia {
  display: flex;
  align-items: flex-start; /* 对齐到顶部 */
}

.el-form-item__label-wrap {
//margin-left: 0px !important;
}
</style>
