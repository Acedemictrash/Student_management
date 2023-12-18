<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <p>
        <span style="font-weight: bold">  &nbsp;&nbsp;&nbsp;&nbsp; 已修学分：{{ data.credit }}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  平均学分绩点：{{ data.gpa }} </span>
      </p>
    </div>
    <el-table v-loading="data.loading"
              element-loading-text="Loading..."
              :element-loading-spinner="svg"
              element-loading-svg-view-box="-10, -10, 50, 50"
              element-loading-background="rgba(122, 122, 122, 0.5)"
              :data="data.tableData" border style="width: 100%; color: #6E77F2 "
              :header-cell-style="{ textAlign: 'center', backgroundColor: 'rgba(110,119,241,.3)'}"
              :cell-style="{ textAlign: 'center', color: '#000'}">
      <el-table-column prop="id" label="选课号" width="140" :resizable="false" />
      <el-table-column prop="name" label="课程名称" width="210" :resizable="false" />
      <el-table-column prop="credit" label="学分" width="80" :resizable="false" />
      <el-table-column prop="teacher" label="教师" width="150" :resizable="false" />
      <el-table-column prop="username" label="学生姓名" width="200" :resizable="false" />
      <el-table-column prop="number" label="学号" width="200" :resizable="false" />
      <el-table-column prop="grade" label="成绩" width="80" :resizable="false" />
      <el-table-column prop="gpa" label="绩点" width="80" :resizable="false" />
      <el-table-column prop="introduction" label="备注" width="265" :resizable="false" />
    </el-table>
    <div class="card" style="margin-top: 10px">
      <el-pagination v-model:current-page="data.pageNum" v-model:page-size="data.pageSize"
                     background layout="prev, pager, next" :total="data.total"
                     @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from "@/utils/request";

const data = reactive({
  loading: false,
  tableData: [],
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

const load = () => {
  const user = JSON.parse(localStorage.getItem('user'))
  data.loading = true
  request.get('/student/selectGrade', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      number: user.number
    }
  }).then(res => {
    data.loading = false
    data.tableData = res.data?.list || [] // ? - null 不报错
    for (let i = 0; i < res.data.list.length; i++) {
      if (res.data.list[i].grade === -1) {
        data.tableData[i].grade = '暂无'
        data.tableData[i].gpa = '暂无'
      }
    }
    data.total = res.data?.total || 0
    data.credit = getCredit()
    data.gpa = getGPA().toFixed(2)
  })
  // 获取后台数据
}

const getCredit = () => {
  let credit = 0
  for (let i = 0; i < data.tableData.length; i++) {
    if (data.tableData[i].grade !== '暂无') {
      credit += data.tableData[i].credit
    }
  }
  return credit
}

const getGPA = () => {
  let gpa = 0
  let allCredit = getCredit()

  for (let i = 0; i < data.tableData.length; i++) {
    if (data.tableData[i].grade !== '暂无') {
      gpa += data.tableData[i].gpa * data.tableData[i].credit / allCredit
    }
  }
  return gpa
}

const handleCurrentChange = (pageNum) => {
  load()
}

load()
</script>