<template>
  <div>
    <div style="padding-left:24px;padding-right:24px;padding-bottom:16px">
      <div style="float:right;padding-right:16px">
        <el-image
          style="width: 100px; height: 80px"
          src="/img/app.png"
          fit="fill"
        />
      </div>
      <h4>文档维护</h4>
      <p>维护和配置与合作伙伴交换的各类文档
      </p>
    </div>

    <el-tabs style="margin-left:24px;margin-right:16px">
      <el-tab-pane label="共享文档">
        <div class="mybody">
          <el-row :gutter="20" style="margin-top:8px">
            <template v-for="item in shareDocs">
              <el-col :key="item.id" :span="8" style="margin-bottom:12px">
                <el-card>

                  <h4>{{ item.docName }} ({{ item.docSysName }})</h4>
                  <p>{{ item.remark?item.remark:'未设置' }} </p>
                  <div>
                    <el-button @click="handleView(item.id)">查看</el-button>
                    <el-button @click="handleEdit(item)">设置</el-button>
                  </div>
                </el-card>
              </el-col>
            </template>

            <el-col :span="8" style="margin-bottom:12px">
              <el-card>
                <h4>添加更多</h4>
                <p>可添加自定义共享文档</p>
                <div>
                  <el-button @click="handleAdd('docShare')">添加</el-button>

                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <el-tab-pane label="协作文档">
        <div class="mybody">
          <el-row :gutter="20">
            <template v-for="item in exchangeDocs">
              <el-col :key="item.id" :span="8" style="margin-bottom:12px">
                <el-card>

                  <h4>{{ item.docName }} ({{ item.docSysName }})</h4>
                  <p>{{ item.remark?item.remark:'未设置' }} </p>
                  <div>
                    <el-button @click="handleView(item.id)">查看</el-button>
                    <el-button @click="handleEdit(item)">设置</el-button>
                  </div>
                </el-card>
              </el-col>
            </template>
            <el-col :span="8" style="margin-bottom:12px">
              <el-card>
                <h4>添加更多</h4>
                <p>可添加自定义协作文档</p>
                <div>
                  <el-button @click="handleAdd('docExchange')">添加</el-button>

                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="系统名称" prop="docSysName">
          <el-input v-model="form.docSysName" placeholder="请输入文档英文名称" />
        </el-form-item>
        <el-form-item label="中文名称" prop="docName">
          <el-input v-model="form.docName" placeholder="请输入文档中文名称" />
        </el-form-item>
        <el-form-item label="字段定义路径" prop="docFieldPath">
          <el-input v-model="form.docFieldPath" placeholder="请输入文档定义路径" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import { listDoc, getDocConfig, addDoc, updateDoc } from '@/api/bizDoc/docConfig'

export default {
  name: 'ShareDoc',

  data() {
    return {
      // 共享文档
      shareDocs: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10

      },

      // 协作文档
      exchangeDocs: [],

      // 是否显示弹出层
      open: false,
      // 弹出层标题
      title: '',
      // 表单参数
      form: {
        shareType: 'docShare'
      },
      rules: {

      }
    }
  },

  created() {
    this.getShareList()
    this.getExchangeList()
  },

  methods: {
    getShareList() {
      this.queryParams.shareType = 'docShare'
      listDoc(this.queryParams).then(response => {
        // console.log('response', response)
        this.shareDocs = response.data
        // console.log('shareDocs', this.shareDocs)
      })
    },

    getExchangeList() {
      this.queryParams.shareType = 'docExchange'
      listDoc(this.queryParams).then(response => {
        this.exchangeDocs = response.data
      })
    },

    getList() {
      this.getShareList()
      this.getExchangeList()
    },

    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        docName: undefined,
        docSysName: undefined,
        shareType: undefined,
        docFieldPath: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },

    /** 新增按钮操作 */
    handleAdd(docType) {
      this.reset()
      this.form.shareType = docType
      this.open = true
      if (docType === 'docShare') {
        this.title = '添加共享文档'
      } else {
        this.title = '添加协同文档'
      }
    },

    /** 查看按钮操作 */
    handleView(docId) {
      this.$router.push('/bizDoc/shareDoc/docList/' + docId)
    },

    /** 修改按钮操作 */
    handleEdit(doc) {
      this.reset()

      getDocConfig(doc.id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改文档配置'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateDoc(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addDoc(this.form).then(response => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
    }

  }
}
</script>

<style  scoped>
.el-card{
    height:180px;
}
</style>>

