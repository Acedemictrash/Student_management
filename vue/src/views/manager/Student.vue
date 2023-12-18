<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin: 5px" v-model="data.username" placeholder="请输入学生名称查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.number" placeholder="请输入学号查询" :prefix-icon="Search"/>
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
        <el-table-column prop="number" label="学号" width="160" :resizable="false" />
        <el-table-column prop="username" label="姓名" width="120" :resizable="false" />
        <el-table-column prop="gender" label="性别" width="120" :resizable="false" />
        <el-table-column prop="phone" label="手机" width="140" :resizable="false" />
        <el-table-column prop="email" label="邮箱" width="230" :resizable="false" />
        <el-table-column prop="grade" label="年级" width="120" :resizable="false" />
        <el-table-column prop="curCredit" label="已修学分" width="120" :resizable="false" />
        <el-table-column prop="maxCredit" label="应修学分" width="120" :resizable="false" />
        <el-table-column prop="gpa" label="GPA" width="120" :resizable="false" />
        <el-table-column prop="avatar" label="头像" width="80" :resizable="false">
          <template #default="scope">
            <el-image v-if="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 5px" :preview-src-list="[scope.row.avatar]" :src="scope.row.avatar" fit="cover" />
          </template>
        </el-table-column>
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
    <el-dialog width="40%" v-model="data.formVisible" title="学生信息" :close-on-click-modal="false" :onClose="dialogClose" destroy-on-close>
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 50px" ref="formRef" :rules="rules" status-icon>
        <el-row>
          <el-col :span="14">
            <el-form-item label="学号" :prop="data.noInput === 1 ? 'noNumber' : 'number'">
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
        <el-row>
          <el-col :span="8">
            <el-form-item label="年级" prop="grade">
              <el-date-picker v-model="data.form.grade" type="year" placeholder="请选择"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="已修学分" prop="credit">
              <el-input v-model="data.form.credit" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="GPA" prop="gpa">
              <el-input v-model="data.form.gpa" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="学生头像" prop="avatar">
          <el-upload action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess" :limit="1" drag>
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              将图片拖拽至这里或 <em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只能上传一个文件
              </div>
            </template>
          </el-upload>
        </el-form-item>
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

<style scoped>
/deep/ .el-upload{
  width: 500px;
  height: 20vh;
}
/deep/ .el-upload .el-upload-dragger{
  width: 500px;
  height: 20vh;
}
</style>

<script setup>
import { reactive, ref, nextTick } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import { UploadFilled } from '@element-plus/icons-vue';

const formRef = ref()
const fullscreenLoading = ref(false)

const idValidator = function (rule, value, callback) {
  request.get('/student/select', {
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
      callback(new Error('学号已存在'))
    }
  })
}

// TODO: 后台排序

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

const rules = reactive({
  number: [
    {required: true, message: '请输入学号', trigger: 'blur'},
    {validator: idValidator, trigger: ['change', 'blur']}
  ],
  username: [
    {required: true, message: '请输入学生名称', trigger: 'blur'}
  ],
  gender: [
    {required: true, message: '请输入性别', trigger: 'blur'}
  ],
  phone: [
    {required: true, message: '请输入电话', trigger: 'blur'},
    {len: 11, message: '请输入正确的手机号', trigger: 'change'}
  ],
  major: [
    {required: true, message: '请输入专业', trigger: 'blur'}
  ],
  grade: [
    {required: true, message: '请选择年级', trigger: 'blur'}
  ],
})

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

const data = reactive({
  noInput: 0,
  number: '',
  username: '',
  form: {},
  formVisible: false,
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  loading: false
})

const load = () => {
  data.loading = true
  request.get('/student/select', {
    params: {
      number: data.number,
      username: data.username,
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || []
    for (let i = 0; i < res.data.list.length; i++) {
      if (res.data.list[i].credit !== null) {
        data.tableData[i].curCredit = res.data.list[i].credit
      } else {
        data.tableData[i].curCredit = null
      }
      data.tableData[i].maxCredit = 140
    }
    data.total = res.data?.total || 0
  })
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      fullscreenLoading.value = true
      request.get('/student/select', {
        params: {
          number: data.form.number,
          name: '',
          username: '',
          pageNum: 1,
          pageSize: 1000
        }
      }).then(res1 => {
        request.request({
          url: res1.data.total === 0 ? '/student/insert' : '/student/update',
          method: res1.data.total === 0 ? 'post' : 'put',
          data: data.form
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

const del = (number) => {
  ElMessageBox.confirm('此操作将永久删除该学生, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/student/delete/' + number).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
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

const resetForm = () => {
  formRef.value.resetFields()
}

const dialogClose = () => {
  data.noInput = 0
  data.formVisible = false
}

const handleEdit = (row) => {
  data.formVisible = true
  data.noInput = 1
  nextTick(() => {
    formRef.value.resetFields()
  })
  data.form = JSON.parse(JSON.stringify(row))
}


const close = () => {
  data.noInput = 0
  formRef.value.resetFields()
  data.formVisible = false
}

const handleCurrentChange = (pageNum) => {
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.noInput = 0
  formRef.value.resetFields()
}

const handleImgUploadSuccess = (res) => {
  data.form.avatar = res.data
}

const reset = () => {
  data.number = ''
  data.username = ''
  load()
}

load()

</script>


