<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin: 5px" v-model="data.username" placeholder="请输入教师名称查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.number" placeholder="请输入教师工号查询" :prefix-icon="Search"/>
      <el-button style="margin: 5px" type="primary" @click="load">查询</el-button>
      <el-button style="margin: 5px" type="info" @click="reset">重置</el-button>
      <el-button style="float: right; margin: 5px" type="primary" @click="handleAdd">新增</el-button>
    </div>
    <div>
      <el-table v-loading="data.loading"
                element-loading-text="Loading..."
                :element-loading-spinner="svg"
                element-loading-svg-view-box="-10, -10, 50, 50"
                element-loading-background="rgba(122, 122, 122, 0.5)"
                :data="data.tableData" border style="width: 100%; color: #6E77F2 "
                :header-cell-style="{ textAlign: 'center', backgroundColor: 'rgba(110,119,241,.3)'}"
                :cell-style="{ textAlign: 'center', color: '#000'}">
        <el-table-column prop="number" label="工号" width="180" />
        <el-table-column prop="username" label="姓名" width="180" />
        <el-table-column prop="gender" label="性别" width="180" />
        <el-table-column prop="phone" label="手机" width="180" />
        <el-table-column prop="email" label="邮箱" width="240" />
        <el-table-column prop="major" label="学院" width="180" />
        <!-- TODO: 展示课程列表 -->
        <el-table-column width="75" :resizable="false">
          <template #default="scope">
            <el-button type="primary" size="small" plain="false" @click="handleEdit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column width="75" :resizable="false">
          <template #default="scope">
            <el-button type="danger" size="small" plain="false" @click="del(scope.row.number)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     background layout="prev, pager, next" :total="data.total"
                     @current-change="handleCurrentChange" />
    </div>

    <el-dialog width="35%" v-model="data.formVisible" title="教师信息" :close-on-click-modal="false" :onClose="dialogClose">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 60px" ref="formRef" :rules="rules" status-icon>
        <el-row>
          <el-col :span="14">
            <el-form-item label="工号" :prop="data.noInput === 1 ? 'noNumber' : 'number'" >
              <el-input v-model="data.form.number" autocomplete="off" :disabled="data.noInput === 1" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="姓名" prop="username">
              <el-input v-model="data.form.username" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="data.form.email" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="data.form.gender" placeholder="请选择">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="data.form.phone" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学院" prop="major">
              <el-input v-model="data.form.major" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="close">取 消</el-button>
          <el-button type="info" @click="resetForm()">重 置</el-button>
          <el-button v-loading.fullscreen.lock="fullscreenLoading" type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()
const fullscreenLoading = ref(false)

const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
      `

const options = [
  {
    value: '男',
    label: '男'
  },
  {
    value: '女',
    label: '女'
  }
]

const data = reactive({
  noInput: 0,
  number: '',
  username: '',
  tableData: [],
  formVisible: false,
  form: {},
  pageNum: 1,
  pageSize: 5,
  loading: false
})

const idValidator = function (rule, value, callback) {
  request.get('/teacher/select', {
    params: {
      number: value,
      name: '',
      username: '',
      pageNum: 1,
      pageSize: 1000
    }
  }).then(res => {
    if (res.data.total === 0) {
      callback()
    } else {
      callback(new Error('工号已存在'))
    }
  })
}


const rules = reactive({
  number: [
    {required: true, message: '请输入工号', trigger: 'blur'},
    {validator: idValidator, trigger: ['change', 'blur']}
  ],
  username: [
    {required: true, message: '请输入教师名称', trigger: 'blur'}

  ],
  gender: [
    {required: true, message: '请输入性别', trigger: 'blur'}
  ],
  phone: [
    {required: true, message: '请输入电话', trigger: 'blur'},
    {len: 11, message: '请输入正确的手机号', trigger: ['change', 'blur']}
  ],
  major: [
    {required: true, message: '请输入学院', trigger: 'blur'}
  ],
})

const load = () => {
  data.loading = true
  request.get('/teacher/select', {
    params: {
      number: data.number,
      username: data.username,
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })
}
const close = () => {
  data.noInput = 0
  formRef.value.resetFields()
  data.formVisible = false
}

const resetForm = () => {
  formRef.value.resetFields()
}

const handleCurrentChange = (pageNum) => {
  load()
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      fullscreenLoading.value = true
      request.get('/teacher/select', {
        params: {
          number: data.form.number,
          name: '',
          username: '',
          pageNum: 1,
          pageSize: 1000
        }
      }).then(res1 => {
        request.request({
          url: res1.data.total === 0 ? '/teacher/insert' : '/teacher/update',
          method: res1.data.total === 0 ? 'post' : 'put',
          data: {
            number: data.form.number,
            username: data.form.username,
            gender: data.form.gender,
            phone: data.form.phone,
            email: data.form.email,
            major: data.form.major
          }
        }).then(res => {
          fullscreenLoading.value = false
          if (res.code === '200') {
            if (res1.data.total === 0) {
              ElMessage.success('添加成功')
            } else {
              ElMessage.success('修改成功')
            }
            close()
            load()
          } else {
            ElMessage.error(res.msg)
          }
        })
      })
    } else {
      return false
    }
  })
}

const handleEdit = (row) => {
  data.formVisible = true
  data.noInput = 1
  formRef.value.resetFields()
  data.form = JSON.parse(JSON.stringify(row))
}

const dialogClose = () => {
  data.noInput = 0
  data.formVisible = false
}

const del = (number) => {
  ElMessageBox.confirm('此操作将永久删除该教师, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/teacher/delete/' + number).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功，请注意更改课程信息')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    })
  })
}

const handleAdd = () => {
  data.formVisible = true
  data.noInput = 0
  formRef.value.resetFields()
}

const reset = () => {
  data.number = ''
  data.username = ''
  load()
}

load()

</script>