<template>
  <div>
    <div class="card" style="padding: 40px 40px">
      <div style="width: 50%">
        <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 50px" ref="formRef" :rules="rules" status-icon>
          <el-form-item label="学生头像" prop="avatar">
            <el-upload class="avatar-uploader" action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess" :limit="1">
              <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar">
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-row>
            <el-col :span="14">
              <el-form-item label="学号" prop="number">
                <el-input v-model="data.form.number" autocomplete="off" :disabled=true />
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
          <el-form-item>
            <el-button type="primary" @click="update">保 存</el-button>
          </el-form-item>
        </el-form>
        <div style="margin-left: 100px">
          <el-button type="primary" @click="data.formVisible=true">修改密码</el-button>
        </div>

      </div>

      <el-dialog width="30%" v-model="data.formVisible" title="修改密码" :close-on-click-modal="false" :onClose="dialogClose">
        <el-form :model="data.form2" label-width="100px" label-position="right" style="padding-right: 25px" ref="formRef" :rules="rules2" status-icon>
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="data.form2.oldPassword" autocomplete="off" show-password />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="data.form2.newPassword" autocomplete="off" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="data.form2.confirmPassword" autocomplete="off" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button type="danger" @click="close">取 消</el-button>
          <el-button v-loading.fullscreen.lock="fullscreenLoading" type="primary" @click="save">保 存</el-button>
        </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { Plus } from "@element-plus/icons-vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {ref, reactive} from "vue";
import Router from "@/router";

const fullscreenLoading = ref(false)
const formRef = ref()

const user = JSON.parse(localStorage.getItem("user"))

const load = () => {
  request.get('/student/select', {
    params: {
      number: user.number,
      username: '',
      pageNum: 1,
      pageSize: 100
    }
  }).then(res => {
    localStorage.setItem('userData', JSON.stringify(res.data.list[0]))
  })
}

const data = reactive({
  form: JSON.parse(localStorage.getItem('userData')),
  form2: {},
  formVisible: false,
})

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

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

const rules = reactive({
  username: [
    {required: true, message: '请输入名称', trigger: 'blur'}
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

const checkPassword = (rule, value, callback) => {
  if (value !== user.password) {
    callback(new Error('旧密码错误'))
  } else {
    callback()
  }
}

const confirmPassword = (rule, value, callback) => {
  if (value !== data.form2.newPassword) {
    callback(new Error('两次密码不一致，请重新输入'))
  } else {
    callback()
  }
}

const checkPassword2 = (rule, value, callback) => {
  const regex = new RegExp("(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}");
  if (value === user.password) {
    callback(new Error('新密码不能与旧密码相同'))
  } else if (!regex.test(value)) {
    callback(new Error('密码必须包含数字和字母，长度为 8-16 位'))
  } else {
    callback()
  }
}

const rules2 = {
  oldPassword: [
    {required: true, message: '请输入旧密码', trigger: 'blur'},
    {validator: checkPassword, trigger: ['change', 'blur']}
  ],
  newPassword: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
    {validator: checkPassword2, trigger: 'blur'}
  ],
  confirmPassword: [
    {required: true, message: '请确认密码', trigger: 'blur'},
    {validator: confirmPassword, trigger: ['change', 'blur']}
  ]
}

// TODO: 变动审核

const handleImgUploadSuccess = (res) => {
  data.form.avatar = res.data
}

const close = () => {
  formRef.value.resetFields()
  data.formVisible = false
}

const logout = () => {
  localStorage.removeItem('userData')
}

const dialogClose = () => {
  formRef.value.resetFields()
  data.formVisible = false
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      fullscreenLoading.value = true
      request.put('/student/update', {
        number: data.form.number,
        password: data.form2.newPassword
      }).then(res => {
        fullscreenLoading.value = false
        if (res.code === '200') {
          ElMessage.success('修改成功，即将跳转到登录页')
          logout()
          setTimeout(() => Router.push('/login'), 2000)
        } else {
          ElMessage.error('修改失败')
        }
      })
    }
  })

}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      fullscreenLoading.value = true
      request.put('/student/update', data.form).then(res => {
        fullscreenLoading.value = false
        if (res.code === '200') {
          ElMessage.success('修改成功，请等待管理员审核')
          load()
          // TODO: 审核机制
        } else {
          ElMessage.error('修改失败')
        }
      })
    }
  })
}

load()

</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>