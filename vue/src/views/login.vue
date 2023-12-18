<template>
  <div>
    <div class="login-container">
      <div class="login-box">
        <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px">登 录</div>
        <el-form :model="data.form" ref="formRef" :rules="rules1">
          <el-form-item prop="number">
            <el-input prefix-icon="User" v-model="data.form.number" placeholder="请输入账号"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="请输入密码"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">登 录</el-button>
          </el-form-item>
          <el-form-item prop="identity">
            <el-radio-group v-model="data.form.identity">
              <el-radio style="margin-left: 30px" label="教师" size="large" />
              <el-radio label="学生" size="large" />
              <el-radio label="管理员" size="large" />
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<!-- TODO: 点了两次怎么办？ -->
<!-- TODO: 机器人验证 -->

<script setup>
import {reactive, ref} from 'vue'
import request from '@/utils/request'
import {ElMessage} from 'element-plus'
import router from "@/router";

const data = reactive({
  form: {},
})

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      //   console.log()
      // }
        request.post('/login', data.form).then(res => {
          if (res.code === '200') {
            localStorage.setItem('user', JSON.stringify(res.data))
            ElMessage.success('登录成功')
            console.log(res.data)
            router.push('/home')
          } else {
            ElMessage.error(res.msg)
          }
        })
    }
  })
}

const formRef = ref()

const rules1 = reactive({
  number: [
    {required: true, message: '请输入账号', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ],
  identity: [
    {required: true, message: '请选择身份', trigger: 'change'}
  ]
})

</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('@/assets/imgs/bg.jpg');
  background-size: 100% 100%;
}

.login-box {
  width: 350px;
  background-color: rgba(255,255,255,.8);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
  border-radius: 5px;
}
</style>
