<template>
  <div>
    <el-table v-loading="data.loading"
              element-loading-text="Loading..."
              :element-loading-spinner="svg"
              element-loading-svg-view-box="-10, -10, 50, 50"
              element-loading-background="rgba(122, 122, 122, 0.5)"
              :data="data.tableData" border style="width: 100%; color: #6E77F2 "
              :header-cell-style="{ textAlign: 'center', backgroundColor: 'rgba(110,119,241,.3)'}"
              :cell-style="{ textAlign: 'center', color: '#000'}">
      <el-table-column prop="cid" label="选课号" width="140" :resizable="false" />
      <el-table-column prop="name" label="课程名称" width="210" :resizable="false" />
      <el-table-column prop="date" label="考试日期" width="250" :resizable="false" />
      <el-table-column prop="introduction" label="考试安排" width="430" :resizable="false">
        <template v-slot="scope">
          <el-date-picker v-model="scope.row.introduction" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss" format="YYYY-MM-DD HH:mm:ss" range-separator="To" start-placeholder="开始时间" end-placeholder="结束时间" readonly />
        </template>
      </el-table-column>
      <el-table-column prop="location" label="考试地点" width="160" :resizable="false" />
      <el-table-column prop="number" label="座位号" width="80" :resizable="false" />
      <el-table-column prop="type" label="考试类型" width="100" :resizable="false" />
      <el-table-column prop="other" label="备注" width="80" :resizable="false" />
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

const load = () => {
  const user = JSON.parse(localStorage.getItem('user'))
  data.loading = true
  request.get('/exam/select', {
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
      if (res.data.list[i].date === null) {
        data.tableData[i].date = '暂无'
      }
      data.tableData[i].introduction = res.data.list[i].introduction.split(',')
    }
    data.total = res.data?.total || 0
  })
  // 获取后台数据
}

const handleCurrentChange = (pageNum) => {
  load()
}

load()
</script>