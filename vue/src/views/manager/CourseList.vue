<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin: 5px" v-model="data.name" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.id" placeholder="请输入选课号查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.username" placeholder="请输入任课教师查询" :prefix-icon="Search"/>
      <el-button style="margin: 5px" type="primary" @click="load">查询</el-button>
      <el-button style="margin: 5px" type="info" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 10px">

    </div>
    <div style="margin-bottom: 5px">
      <el-menu default-active="1" mode="horizontal" @select="handleSelect">
        <el-menu-item index="1">全部课程</el-menu-item>
        <el-menu-item index="2">已选课程</el-menu-item>
        <el-menu-item index="3">未选课程</el-menu-item>
      </el-menu>
    </div>

    <!--    管理员看的页面-->
    <div style="margin-bottom: 10px">
      <el-table v-loading="data.loading"
                element-loading-text="Loading..."
                :element-loading-spinner="svg"
                element-loading-svg-view-box="-10, -10, 50, 50"
                element-loading-background="rgba(122, 122, 122, 0.5)"
                :data="data.tableData" border style="width: 100%; color: #6E77F2 "
                :header-cell-style="{ textAlign: 'center', backgroundColor: 'rgba(110,119,241,.3)'}"
                :cell-style="{ textAlign: 'center', color: '#000'}">
        <el-table-column prop="id" label="选课号" width="140" :resizable="false" />
        <el-table-column prop="name" label="名称" width="210" :resizable="false" />
        <el-table-column prop="credit" label="学分" width="60" :resizable="false" />
        <el-table-column prop="teacher" label="教师" width="80" :resizable="false" />
        <el-table-column prop="week" label="上课时间（上课周）" width="200" :resizable="false" />
        <el-table-column prop="classroom" label="教室" width="140" :resizable="false" />
        <el-table-column prop="maxNum" label="容量" width="60" :resizable="false" />
        <el-table-column prop="currentNum" label="已选" width="60" :resizable="false" />
        <el-table-column prop="leftNum" label="剩余" width="60" :resizable="false" />
        <el-table-column prop="introduction" label="备注" width="265" :resizable="false" />
        <el-table-column width="75" :resizable="false" label="操作">
          <template #default="scope">
            <el-button v-if="isSelect(scope.row)" type="primary" size="small" @click="select(scope.row)">选课</el-button>
            <el-button v-else type="danger" size="small" @click="deleteSelect(scope.row)" >退课</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="showWeight" label="权重" width="60" :resizable="false" />
        <el-table-column width="75" :resizable="false" label="操作">
          <template #default="scope">
            <el-button type="info" size="small" @click="modify(scope.row)" :disabled="isSelect(scope.row)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     background layout="prev, pager, next" :total="data.total"
                     @current-change="handleCurrentChange" />
    </div>
    <el-dialog width="30%" v-model="data.formVisible" title="选课" :close-on-click-modal="false" :onClose="dialogClose">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-left: 50px" ref="formRef" :rules="rules" status-icon>
        <div style="margin-bottom: 20px">
          <p style="margin-left: 30px; display: inline">剩余权重：{{ leftWeight }} </p>
          <p style="color: #3BB201; display: inline;"> (+{{ data.originWeight }})</p>
        </div>

        <el-row>
          <el-col :span="14">
            <el-form-item label="权重" prop="weight" >
              <el-input v-model.number="data.form.weight" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="close">取 消</el-button>
          <el-button v-loading.fullscreen.lock="fullscreenLoading" type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from "element-plus";
import request from "@/utils/request";
const handleCurrentChange = (pageNum) => {
  load()
}

const user = JSON.parse(localStorage.getItem('user') || '{}')

const formRef = ref()
const fullscreenLoading = ref(false)
const leftWeight = ref(user.leftWeight)

const data = reactive({
  cid: '',
  teacherName: '',
  originWeight: 0,
  form: {},
  formVisible: false,
  name: '',
  id: '',
  sid: '',
  username: '',
  tableData: [{
      index: ''
    }
  ],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  loading: false,
});

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

const checkWeight = (rule, value, callback) => {
  if (value < 0 || value > 100) {
    callback(new Error('请输入 0 - 100 之间的整数'))
  } else if (value > user.leftWeight + data.originWeight) {
    callback(new Error('剩余权重不足'))
  } else {
    callback()
  }
}

const rules = reactive({
  weight: [
    { required: true, message: '请输入权重', trigger: 'blur' },
    { type: 'number', message: '请输入 0 - 100 之间的整数', trigger: 'blur' },
    { validator: checkWeight, trigger: ['change', 'blur'] }
  ]
})

const getTime = (res, idx) => {
  let week = []
  let times = Array.from(new Set(res.data.list[idx].time.map(JSON.stringify))).map(JSON.parse)
  for (let i = 0; i < times.length; i++) {
    let time = times[i]
    let str = ""
    if (time.monday === 1) {
      str += "周一、"
    }
    if (time.tuesday === 1) {
      str += "周二、"
    }
    if (time.wednesday === 1) {
      str += "周三、"
    }
    if (time.thursday === 1) {
      str += "周四、"
    }
    if (time.friday === 1) {
      str += "周五、"
    }
    if (time.saturday === 1) {
      str += "周六、"
    }
    if (time.sunday === 1) {
      str += "周日、"
    }
    str = str.slice(0, str.length - 1)
    str += " "
    if (time.startTime !== time.endTime) {
      str += time.startTime + "-" + time.endTime + " 节"
    } else {
      str += time.startTime + " 节"
    }
    if (time.startWeek !== time.endWeek) {
      str += " (" + time.startWeek + "-" + time.endWeek + ")"
    } else {
      str += " (" + time.startWeek + ")"
    }


    week.push(str)
  }

  return week.join("\r\n")
}

// TODO: 课表 -> 冲突校验
// TODO: 管理员发布考试
// TODO: 教师的课程管理
// TODO: 审批流程
// TODO: 登录拦截器

const load = () => {
  data.loading = true
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      id: data.id,
      username: data.username,
      sid: data.sid
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || [] // ? - null 不报错
    for (let i = 0; i < res.data.list.length; i++) {
      data.tableData[i].week = getTime(res, i) || []
      data.tableData[i].teacher = res.data?.list[i].username || []
      data.tableData[i].leftNum = data.tableData[i]?.maxNum - data.tableData[i].currentNum || []
      data.tableData[i].index = i
      data.tableData[i].showWeight = res.data.list[i].weight === -1 ? 0 : res.data.list[i].weight
    }
    data.total = res.data?.total || 0
  })
  // 获取后台数据
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      fullscreenLoading.value = true
      request.get('/course/getSelect', {
        params: {
          sid: user.number,
          cid: data.cid
        }
      }).then(res1 => {
        request.request({
          url: res1.data === 0 ? '/course/addSelect' : '/course/updateSelect',
          method: res1.data === 0 ? 'post' : 'put',
          data: {
            sid: user.number,
            tname: data.teacherName,
            cid: data.cid,
            weight: data.form.weight,
            originalWeight: data.originWeight
          }
        }).then(res => {
          fullscreenLoading.value = false
          if (res.code === '200') {
            if (res1.data === 0) {
              ElMessage.success('选课成功')
            } else {
              ElMessage.success('修改成功')
            }
            user.leftWeight += data.originWeight - data.form.weight
            localStorage.setItem('user', JSON.stringify(user))
            close()
            load()
          } else {
            ElMessage.error(res.msg)
          }
        })
      })
    }
  })
}

const select = (row) => {
  if (row.currentNum === row.maxNum) {
    ElMessage.warning('请注意，课程人数已超出上限')
  }

  data.originWeight = 0
  data.cid = row.id
  data.teacherName = row.username
  data.formVisible = true
}

const modify = (row) => {
  data.cid = row.id
  data.originWeight = row.weight
  data.formVisible = true
}

const isSelect = (row) => {
  return row.weight === -1
}

const dialogClose = () => {
  data.formVisible = false
}

const deleteSelect = (row) => {
  ElMessageBox.confirm('此操作将退选该课程, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/course/deleteSelect', {
      params: {
        sid: user.number,
        cid: row.id,
        originalWeight: row.weight
      }
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('退课成功')
        user.leftWeight += row.weight
        localStorage.setItem('user', JSON.stringify(user))
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消退课'
    })
  })
}

const handleSelect = (index) => {
  if (index === "1") {
    data.sid = ''
    load()
  } else if (index === "2") {
    data.sid = user.number
    load()
  } else if (index === "3") {
    data.sid = "-" + user.number
    load()
  }
}

const close = () => {
  formRef.value.resetFields()
  data.formVisible = false
}

const reset = () => {
  data.name = ''
  data.id = ''
  data.username = ''
  load()
}

load()
</script>

<style>
.el-table .cell {
  white-space: pre-line;
}
</style>
