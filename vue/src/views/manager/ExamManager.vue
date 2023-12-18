<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin: 5px" v-model="data.name" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.id" placeholder="请输入选课号查询" :prefix-icon="Search"/>
      <el-button style="margin: 5px" type="primary" @click="load">查询</el-button>
      <el-button style="margin: 5px" type="info" @click="reset">重置</el-button>
      <el-button style="float: right; margin: 5px" type="primary" @click="handleAdd">新增</el-button>
    </div>
    <el-table v-loading="data.loading"
              element-loading-text="Loading..."
              :element-loading-spinner="svg"
              element-loading-svg-view-box="-10, -10, 50, 50"
              element-loading-background="rgba(122, 122, 122, 0.5)"
              :data="data.tableData" border style="width: 100%; color: #6E77F2 "
              :header-cell-style="{ textAlign: 'center', backgroundColor: 'rgba(110,119,241,.3)'}"
              :cell-style="{ textAlign: 'center', color: '#000'}">
      <el-table-column prop="cid" label="选课号" width="180" :resizable="false" />
      <el-table-column prop="name" label="课程名称" width="220" :resizable="false" />
      <el-table-column prop="date" label="考试日期" width="250" :resizable="false" />
      <el-table-column prop="introduction" label="考试安排" width="450" :resizable="false">
        <template v-slot="scope">
          <el-date-picker v-model="scope.row.introduction" type="datetimerange" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" range-separator="To" start-placeholder="开始时间" end-placeholder="结束时间" readonly />
        </template>
      </el-table-column>
      <el-table-column prop="type" label="考试类型" width="115" :resizable="false" />
      <el-table-column prop="other" label="备注" width="120" :resizable="false" />
      <el-table-column width="80" :resizable="false">
      <template #default="scope">
        <el-button type="primary" size="small" plain="false" @click="handleEdit(scope.row)">编辑</el-button>
      </template>
      </el-table-column>
      <el-table-column width="80" :resizable="false">
        <template #default="scope">
          <el-button type="danger" size="small" plain="false" @click="del(scope.row.cid)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="card" style="margin-top: 10px">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     background layout="prev, pager, next" :total="data.total"
                     @current-change="handleCurrentChange" />
    </div>
    <el-dialog width="35%" v-model="data.formVisible" title="考试信息" :close-on-click-modal="false" :onClose="dialogClose">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 60px" ref="formRef" :rules="rules" status-icon>
        <el-form-item label="课程" prop="class">
          <el-select v-model="data.form.class" style="width: 100%" remote-show-suffix clearable filterable remote placeholder="请选择" :remote-method="searchClass" :loading="data.searchLoading">
            <el-option v-for="item in classes.class" :key="item.key" :label="item.value" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-alert style="margin-left: 100px; margin-bottom: 15px; width: max-content" title="提示" type="info" description="必须存在对应课程的信息，才能添加考试" show-icon />
        <el-form-item label="时间" prop="time">
          <el-date-picker v-model="data.form.time" type="datetimerange" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" range-separator="To" start-placeholder="开始时间" end-placeholder="结束时间"/>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="data.form.type" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="other">
          <el-input v-model="data.form.other" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="close">取 消</el-button>
          <el-button type="info" @click="resetForm">重 置</el-button>
          <el-button v-loading.fullscreen.lock="fullscreenLoading" type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {nextTick, reactive, ref} from 'vue'
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Search} from "@element-plus/icons-vue";

const formRef = ref()
const fullscreenLoading = ref(false)

const classes = reactive({
  class: [],
})

const rules = {
  class: [
    { required: true, message: '请选择课程', trigger: 'blur' }
  ],
  time: [
    { required: true, message: '请选择时间', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择类型', trigger: 'blur' }
  ],
}

const data = reactive({
  name: '',
  id: '',
  loading: false,
  form: {
    class: '',
  },
  formVisible: false,
  searchLoading: false,
  tableData: [{
    introduction: [],
  }
  ],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  credit: '',
  gpa: ''
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

const options = [
  {
    value: '期中考试',
    label: '期中考试'
  },
  {
    value: '期末考试',
    label: '期末考试'
  }
]

const load = () => {
  data.loading = true
  request.get('/exam/select', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      cid: data.id === '' ? '' : data.id,
      name: data.name === '' ? '' : data.name,
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || [] // ? - null 不报错
    for (let i = 0; i < res.data.list.length; i++) {
      data.tableData[i].introduction = res.data.list[i].introduction.split(',')
    }
    data.total = res.data?.total || 0
  })
  // 获取后台数据
}

const searchClass = (name) => {
  data.searchLoading = true
  request.get('/course/selectPage', {
    params: {
      name: name,
    }
  }).then(res => {
    data.searchLoading = false
    for (let i = 0; i < res.data.list.length; i++) {
      let str = []
      let item = [{key: '', value: ''}]
      str.push(res.data.list[i].id)
      str.push(res.data.list[i].name)
      str.push(res.data.list[i].username)
      item.key = res.data.list[i].id
      item.value = str.join(", ")
      classes.class[i] = item
    }
  })
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      fullscreenLoading.value = true
      request.get('/exam/select', {
        params: {
          pageNum: data.pageNum,
          pageSize: data.pageSize,
          cid: data.form.class.split(", ")[0],
          name: ''
        }
      }).then(res => {
        request.request({
          url: res.data.list.length === 0 ? '/exam/insert' : '/exam/update',
          method: res.data.list.length === 0 ? 'post' : 'put',
          data: {
              cid: data.form.class.split(", ")[0],
              name: data.form.class.split(", ")[1],
              introduction: data.form.time.join(','),
              type: data.form.type,
              other: data.form.other,
          }
        }).then(res0 => {
          fullscreenLoading.value = false
          if (res0.code === '200') {
            if (res.data.list.length === 0) {
              ElMessage.success('添加成功')
            } else {
              ElMessage.success('修改成功')
            }
            formRef.value.resetFields()
            load()
            dialogClose()
          } else {
            if (res.data.list.length === 0) {
              ElMessage.error('添加失败')
            } else {
              ElMessage.error('修改失败')
            }
          }
        })
      })
    } else {
      return false
    }
  })

}

const handleAdd = () => {
  data.formVisible = true
}

const handleCurrentChange = (pageNum) => {
  load()
}

const handleEdit = (row) => {
  data.formVisible = true
  nextTick(() => {
    formRef.value.resetFields()
  })
  console.log(JSON.parse(JSON.stringify(row)))
  data.form = JSON.parse(JSON.stringify(row))
  data.form.class = row.cid + ', ' + row.name
  data.form.time = row.introduction
}

const del = (id) => {
  ElMessageBox.confirm('此操作将永久删除该学生, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/exam/delete', {
      params: {
        cid: id,
      }
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error('删除失败')
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    })
  })
}

const dialogClose = () => {
  formRef.value.resetFields()
  data.formVisible = false
}

const resetForm = () => {
  formRef.value.resetFields()
}

const reset = () => {
  data.name = ''
  data.id = ''
  load()
}

load()
</script>