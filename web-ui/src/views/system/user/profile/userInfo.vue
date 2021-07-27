
<template>

  <el-form ref="form" :model="formData" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称">
      <el-input v-model="formData.nickName" />
    </el-form-item>
    <el-form-item label="手机号码" prop="phoneNumber">
      <el-input v-model="formData.phoneNumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="formData.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="formData.sex">

        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>

  </el-form>
</template>

<script>
import { updateUserProfile } from '@/api/system/user'

export default {
  props: {
    // eslint-disable-next-line vue/require-default-prop
    user: {
      type: Object
    }
  },
  data() {
    return {
      formData: {
        nickName: '',
        phoneNumber: '',
        email: '',
        sex: '0'
      },
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
          {
            type: 'email',
            message: "'请输入正确的邮箱地址",
            trigger: ['blur', 'change']
          }
        ],
        phoneNumber: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ]
      }
    }
  },

  watch: {
    // 如果 `question` 发生改变，这个函数就会运行
    user: function(newQuestion, oldQuestion) {
      this.formData = this.user
      console.log('formData', this.formData)
    }
  },
  methods: {
    submit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          updateUserProfile(this.formData).then(response => {
            this.msgSuccess('修改成功')
          })
        }
      })
    },
    close() {
      this.$store.dispatch('tagsView/delView', this.$route)
      this.$router.push({ path: '/index' })
    }
  }
}
</script>
