<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px; margin: 5px" v-model="data.name" placeholder="请输入课程名称查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.id" placeholder="请输入选课号查询" :prefix-icon="Search"/>
      <el-input style="width: 260px; margin: 5px" v-model="data.username" placeholder="请输入任课教师查询" :prefix-icon="Search"/>
      <el-button style="margin: 5px" type="primary" @click="load">查询</el-button>
      <el-button style="margin: 5px" type="info" @click="reset">重置</el-button>
      <el-button style="float: right; margin: 5px" type="primary" @click="handleAdd">新增</el-button>
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
        <el-table-column prop="week" label="上课周" width="180" :resizable="false" />
        <el-table-column prop="classroom" label="教室" width="140" :resizable="false" />
        <el-table-column prop="maxNum" label="容量" width="60" :resizable="false" />
        <el-table-column prop="currentNum" label="已选" width="60" :resizable="false" />
        <el-table-column prop="leftNum" label="剩余" width="60" :resizable="false" />
        <el-table-column prop="introduction" label="备注" width="255" :resizable="false" />
        <el-table-column width="72" :resizable="false">
          <template #default="scope">
            <el-button type="primary" size="small" plain="false" @click="handleEdit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column width="72" :resizable="false">
          <template #default="scope">
            <el-button type="danger" size="small" plain="false" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
        <el-table-column width="96" :resizable="false">
          <template #default="scope">
            <el-button type="info" size="small" plain="false" @click="showStudent(scope.row.id)">学生列表</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <div class="card">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     background layout="prev, pager, next" :total="data.total"
                     @current-change="handleCurrentChange" />
    </div>

    <el-dialog width="40%" v-model="data.formVisible" title="课程信息" :close-on-click-modal="false" :onClose="dialogClose">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px" ref="formRef1" :rules="rules1" status-icon>
        <el-row :gutter="1">
          <el-col :span="10">
            <el-form-item label="选课号" :prop="data.noInput === 0 ? 'id' : 'noId'">
              <el-input v-model="data.form.id" autocomplete="off" :disabled="data.noInput === 1" />
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <el-form-item label="课程名称" prop="name">
              <el-input v-model="data.form.name" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="1">
          <el-col :span="9">
            <el-form-item label="任课教师" prop="username">
              <el-select v-model="data.form.username" remote-show-suffix clearable filterable remote placeholder="请选择" :remote-method="searchTeacher" :loading="data.searchLoading">
                <el-option v-for="item in data.teacherName" :key="item.number" :label="item.username" :value="item.username" />
              </el-select>
            </el-form-item>
            <el-alert style="margin-left: 30px; margin-bottom: 15px; width: max-content" title="提示" type="info" description="必须存在对应老师的信息，才能添加课程" show-icon />
          </el-col>
          <el-col :span="15">
            <el-form-item label="教室" prop="classroom">
              <el-input v-model="data.form.classroom" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="课程描述" prop="introduction">
          <el-input v-model="data.form.introduction" autocomplete="off" />
        </el-form-item>
        <el-row>
          <el-col :span="10">
            <el-form-item label="学分" prop="credit">
              <el-input v-model.number="data.form.credit" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <el-form-item label="容量" prop="maxNum">
              <el-input v-model.number="data.form.maxNum" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div style="margin-left: 45px">
        <el-form  v-for="(time, index) in timeForm.times" ref="formRef2"
                  :key="index" :model="timeForm" :label="'time' + index">
          <el-row>
            <el-col :span="12">
              <el-form-item label="起始周" :prop="'times.' + index + '.startWeek'"
                            :rules="[{ required: true, message: '请输入起始周', trigger: 'blur' }, { validator: numberRule1.validator(index), trigger: 'change'}]">
                <el-input-number v-model="time.startWeek" :min="1" :max="22" :step="1" step-strictly />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束周" :prop="'times.' + index + '.endWeek'"
                            :rules="[{ required: true, message: '请输入结束周', trigger: 'blur' }, { validator: numberRule2.validator(index), trigger: 'change'}]">
                <el-input-number v-model="time.endWeek" :min="1" :max="22" :step="1" step-strictly ref="endWeek" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item label="上课时间（星期）" :prop="'times.' + index + '.week'"
                            :rules="[{ required: true, message: '请选择上课时间', trigger: ['blur', 'change']}]">
                <el-select v-model="time.week" multiple collapse-tags collapse-tags-tooltip placeholder="请选择">
                  <el-option v-for="item in options" :key="item.key" :label="item.label" :value="item.label"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="开始时间（节）" :prop="'times.' + index + '.startTime'"
                            :rules="[{required: true, message: '请输入开始时间', trigger: 'blur'}, { validator: numberRule3.validator(index), trigger: 'change'}]">
                <el-input-number v-model="time.startTime" :min="1" :max="12" :step="1" step-strictly />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间（节）" :prop="'times.' + index + '.endTime'"
                            :rules="[{required: true, message: '请输入结束时间', trigger: 'blur'}, { validator: numberRule4.validator(index), trigger: 'change'}]">
                <el-input-number v-model="time.endTime" :min="1" :max="12" :step="1" step-strictly />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button @click="removeTime(index)" type="danger" plain="false">删除时间</el-button>
          </el-form-item>
          <el-divider />
        </el-form >
        <el-form>
          <el-form-item>
            <el-button @click="addTime" type="primary" plain="false">添加时间</el-button>
          </el-form-item>
        </el-form>

      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="close">取 消</el-button>
          <el-button type="info" @click="resetForm()">重 置</el-button>
          <el-button v-loading.fullscreen.lock="fullscreenLoading" type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog width="35%" v-model="data.stuTableVisible" title="学生列表" :close-on-click-modal="false" :onClose="tableClose">
      <el-table v-loading="stuTable.loading"
                element-loading-text="Loading..."
                :element-loading-spinner="svg"
                element-loading-svg-view-box="-10, -10, 50, 50"
                element-loading-background="rgba(122, 122, 122, 0.5)"
                :data="stuTable.stuTableData" border style="width: 100%; color: #6E77F2 "
                :header-cell-style="{ textAlign: 'center', backgroundColor: 'rgba(110,119,241,.3)'}"
                :cell-style="{ textAlign: 'center', color: '#000'}">
        <el-table-column prop="sid" label="学号" width="180" :resizable="false" />
        <el-table-column prop="username" label="姓名" width="120" :resizable="false" />
        <el-table-column prop="grade" label="成绩" width="90" :resizable="false" />
        <el-table-column width="80" :resizable="false" label="操作">
          <template #default="scope">
            <el-button type="info" size="small" @click="modify(scope.row)">修改</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="权重" width="90" :resizable="false" />
      </el-table>
      <div class="card" style="margin-top: 10px">
        <el-pagination v-model:current-page="stuTable.pageNum" v-model:page-size="stuTable.pageSize"
                       background layout="prev, pager, next" :total="stuTable.total"
                       @current-change="handleCurrentChange2" />
      </div>
    </el-dialog>

    <el-dialog width="30%" v-model="data.gradeVisible" title="成绩" :close-on-click-modal="false" :onClose="tableClose2">
      <div style="margin-left: 80px">
        <el-form :model="data.form2" label-width="100px" label-position="right" style="padding-right: 25px" ref="formRef3" :rules="rules3" status-icon>
          <p style="margin-left: 45px"> 原成绩：{{ data.grade !== undefined ? data.grade : '' }} </p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="新成绩" prop="grade">
                <el-input v-model.number="data.form2.grade" autocomplete="off" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="close2">取 消</el-button>
          <el-button v-loading.fullscreen.lock="fullscreenLoading2" type="primary" @click="saveGrade">保 存</el-button>
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

const fullscreenLoading = ref(false)
const fullscreenLoading2 = ref(false)
const formRef1 = ref()
const formRef2 = ref()
const formRef3 = ref()
const rowIndex = ref(0);

const handleCurrentChange = (pageNum) => {
  load()
}

const handleCurrentChange2 = (pageNum) => {
  load2()
}

const numberRule1 = {
  validator: (index) => {
    return (rule, value, callback) => {
      if (value > timeForm.times[index].endWeek) {
        callback(new Error('起始周不能大于结束周'))
      } else {
        callback()
      }
    }
  }
}

const numberRule2 = {
  validator: (index) => {
    return (rule, value, callback) => {
      if (value < timeForm.times[index].startWeek) {
        callback(new Error('结束周不能小于起始周'))
      } else {
        callback()
      }
    }
  }
}

const numberRule3 = {
  validator: (index) => {
    return (rule, value, callback) => {
      if (value > timeForm.times[index].endTime) {
        callback(new Error('开始时间不能大于结束时间'))
      } else {
        callback()
      }
    }
  }
}

const numberRule4 = {
  validator: (index) => {
    return (rule, value, callback) => {
      if (value < timeForm.times[index].startTime) {
        callback(new Error('结束时间不能小于开始时间'))
      } else {
        callback()
      }
    }
  }
}

const options = [
  {
    key: '1',
    label: '星期一'
  },
  {
    key: '2',
    label: '星期二'
  },
  {
    key: '3',
    label: '星期三'
  },
  {
    key: '4',
    label: '星期四'
  },
  {
    key: '5',
    label: '星期五'
  },
  {
    key: '6',
    label: '星期六'
  },
  {
    key: '7',
    label: '星期日'
  }
]

const idValidator = function (rule, value, callback) {
  request.get('/course/selectPage', {
    params: {
      id: value,
      name: '',
      username: '',
      sid: '',
      pageNum: 1,
      pageSize: 1000
    }
  }).then(res => {
    if (res.data.total === 0) {
      callback()
    } else {
      callback(new Error('选课号已存在'))
    }
  })
}


const rules1 = reactive({
  id: [
    {required: true, message: '请输入选课号', trigger: 'blur'},
    {validator: idValidator, trigger: ['change', 'blur']}
  ],
  name: [
    {required: true, message: '请输入课程名称', trigger: 'blur'}
  ],
  username: [
    {required: true, message: '请选择任课教师', trigger: ["blur", 'change']}
  ],
  classroom: [
    {required: true, message: '请输入教室', trigger: 'blur'}
  ],
  credit: [
    {required: true, message: '请输入学分', trigger: 'blur'},
    {type: 'number', message: '学分必须为数字值', trigger: 'blur'}
  ],
  maxNum: [
    {required: true, message: '请输入容量', trigger: 'blur'},
    {type: 'number', message: '容量必须为数字值', trigger: 'blur'}
  ]
})

const gradeValidator = function (rule, value, callback) {
  if (value > 100 || value < 0) {
    callback(new Error('成绩必须在 0 - 100之间'))
  } else {
    callback()
  }
}

const rules3 = {
  grade: [
    {required: true, message: '请输入成绩', trigger: 'blur'},
    {type: 'number', message: '成绩必须为数字值', trigger: 'blur'},
    {validator: gradeValidator, trigger: ['change', 'blur']}
  ]
}

const removeTime = (index) => {
  if (index !== 0) {
    timeForm.times.splice(index, 1)
  } else {
    ElMessage({
      message: '至少保留一个时间段',
      type: 'warning'
    })
  }
}

const addTime = () => {
  timeForm.times.push({
    key: timeForm.index + 1,
    startWeek: 1,
    endWeek: 1,
    startTime: 1,
    endTime: 1
  })
  timeForm.index += 1
}

const searchTeacher = (name) => {
  data.searchLoading = true
  request.get('/teacher/selectbyname', {
    params: {
      username: name,
    }
  }).then(res => {
    data.searchLoading = false
    data.teacherName = res.data
  })
}

const timeForm = reactive({
  times: [
    {
      week: [],
      startWeek: 1,
      endWeek: 1,
      startTime: 1,
      endTime: 1,
      key: 1
    },
  ],
  index: 1
})

const stuTable = reactive({
  stuTableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 15,
  loading: false
})

const data = reactive({
  name: '',
  id: '',
  username: '',
  form: {
    id: '',
    name: '',
    username: '',
    classroom: '',
    credit: '',
    maxNum: '',
    introduction: ''
  },
  grade: localStorage.getItem('grade'),
  form2: {
    cid: '',
    sid: '',
    grade: '',
  },
  noInput: 0,
  formVisible: false,
  stuTableVisible: false,
  gradeVisible: false,
  teacherName: [],
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 5,
  loading: false,
  searchLoading: false,
  saveLoading: false
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

const getWeek = (res, idx) => {
  let week = []
  let arr = []
  week.push("第 ")
  for (let i = 1; i <= 22; i++) {
    arr[i] = 0
  }

  for (let i = 0; i < res.data.list[idx].timeTotal; i++) {
    let start = res.data.list[idx].time[i].startWeek
    let end = res.data.list[idx].time[i].endWeek
    for (let j = start; j <= end; j++) {
      arr[j] = 1
    }
  }

  let i = 1, j = 2
  while (j <= 22) {
    if (arr[i] === 1) {
      if (arr[j] === 1) {
        j++
        if (j === 23) {
          week.push(i + " - " + (j - 1))
        }
      } else {
        if (j - i === 1) {
          week.push(i)
          week.push(", ")
        } else {
          week.push(i + " - " + (j - 1))
          week.push(", ")
        }
        i = j
        j++
      }
    } else {
      i++
      j++
      if (arr[i] === 1 && j === 23) {
        week.push(i)
      }
    }
  }

  if (week[week.length - 1] === ", ") {
    week.pop()
  }
  week.push(" 周")

  return week.join("")
}

const load2 = () => {
  stuTable.loading = true
  request.get('/course/getSelectByCid', {
    params: {
      cid: rowIndex.value,
      pageNum: stuTable.pageNum,
      pageSize: stuTable.pageSize
    }
  }).then(res => {
    stuTable.loading = false
    stuTable.stuTableData = res.data?.list || []
  })
}

const showStudent = (id) => {
  data.stuTableVisible = true
  data.form2.cid = id;
  stuTable.pageNum = 1
  stuTable.pageSize = 15
  rowIndex.value = id
  load2()
}

const load = () => {
  data.loading = true
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      id: data.id,
      username: data.username,
      sid: '',
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || [] // ? - null 不报错
    for (let i = 0; i < res.data.list.length; i++) {
      data.tableData[i].week = getWeek(res, i) || []
      data.tableData[i].teacher = res.data?.list[i].username || []
      data.tableData[i].leftNum = data.tableData[i]?.maxNum - data.tableData[i].currentNum || []
    }
    data.total = res.data?.total || 0
  })
  // 获取后台数据
}

const save = () => {
  formRef1.value.validate((valid) => {
    if (!valid) {
      return false
    } else {
      for (let i = 0; i < timeForm.times.length; i++) {
        formRef2.value[i].validate((valid) => {
          if (!valid) {
            return false
          }
        })
      }
      saveCourse()
    }
  })
}

const saveCourse = () => {
  fullscreenLoading.value = true
  request.get('/course/selectPage', {
    params: {
      id: data.form.id,
      name: '',
      username: '',
      sid: '',
      pageNum: 1,
      pageSize: 1000
    }
  }).then(res1 => {
    request.request({
      url: res1.data.total === 0 ? '/course/insert' : '/course/update',
      method: res1.data.total === 0 ? 'post' : 'put',
      data: {
        id: data.form.id,
        name: data.form.name,
        username: data.form.username,
        classroom: data.form.classroom,
        credit: data.form.credit,
        maxNum: data.form.maxNum,
        currentNum: 0,
        introduction: data.form.introduction,
        time: timeForm.times
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
}

const handleAdd = () => {
  data.formVisible = true
  data.noInput = 0
  formRef1.value.resetFields()
  for (let i = 1; i < timeForm.times.length; i++) {
    timeForm.times.splice(i, 1)
  }
  formRef2.value[0].resetFields()
}

const resetForm = () => {
  formRef1.value.resetFields()
  for (let i = 1; i < timeForm.times.length; i++) {
    timeForm.times.splice(i, 1)
  }
  formRef2.value[0].resetFields()
}

const close = () => {
  data.noInput = 0
  formRef1.value.resetFields()
  for (let i = 1; i < timeForm.times.length; i++) {
    timeForm.times.splice(i, 1)
  }
  formRef2.value[0].resetFields()
  data.formVisible = false
}

const close2 = () => {
  formRef3.value.resetFields()
  data.gradeVisible = false
}

const handleEdit = (row) => {
  data.formVisible = true
  data.noInput = 1
  formRef1.value.resetFields()
  for (let i = 1; i < timeForm.times.length; i++) {
    timeForm.times.splice(i, 1)
  }
  formRef2.value[0].resetFields()

  data.form = JSON.parse(JSON.stringify(row))

  row.time = Array.from(new Set(row.time.map(JSON.stringify))).map(JSON.parse)

  timeForm.times = []
  for (let i = 0; i < row.time.length; i++) {
    timeForm.times.push({})
    let week = []
    if (row.time[i].monday === 1) {
      week.push("星期一")
    }
    if (row.time[i].tuesday === 1) {
      week.push("星期二")
    }
    if (row.time[i].wednesday === 1) {
      week.push("星期三")
    }
    if (row.time[i].thursday === 1) {
      week.push("星期四")
    }
    if (row.time[i].friday === 1) {
      week.push("星期五")
    }
    if (row.time[i].saturday === 1) {
      week.push("星期六")
    }
    if (row.time[i].sunday === 1) {
      week.push("星期日")
    }
    timeForm.times[i].week = week
    timeForm.times[i].startWeek = row.time[i].startWeek
    timeForm.times[i].endWeek = row.time[i].endWeek
    timeForm.times[i].startTime = row.time[i].startTime
    timeForm.times[i].endTime = row.time[i].endTime
    timeForm.times[i].key = i + 1
  }
}

const del = (id) => {
  ElMessageBox.confirm('此操作将永久删除该课程, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/course/delete/' + id ).then(res => {
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

const saveGrade = () => {
  formRef3.value.validate((valid) => {
    if (valid) {
      fullscreenLoading2.value = true
      console.log(data.form2)
      request.put('/course/updateGrade', {
        sid: data.form2.sid,
        cid: data.form2.cid,
        grade: data.form2.grade
      }).then(res => {
        fullscreenLoading2.value = false
        if (res.code === '200') {
          ElMessage.success('修改成功')
          data.grade = data.form2.grade
          localStorage.setItem('grade', data.form2.grade)
          tableClose2()
          load2()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const modify = (row) => {
  localStorage.setItem('grade', row.grade !== undefined ? row.grade : '')
  data.form2.sid = row.sid
  data.gradeVisible = true
}

const tableClose = () => {
  data.stuTableVisible = false
}

const tableClose2 = () => {
  formRef3.value.resetFields()
  data.gradeVisible = false
}

const dialogClose = () => {
  data.noInput = 0
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

