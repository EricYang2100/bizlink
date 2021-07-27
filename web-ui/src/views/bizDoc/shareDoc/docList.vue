<template>
  <div class="app-container">
    <!-- 文档名称 -->
    <div>
      <h3>{{ docConfig.docName }} </h3>
    </div>

    <!-- 查询条件 -->
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <template v-for="(field,index) in queryParams.data">

        <el-form-item :key="'query'+field.key" :label="field.title" :prop="field.sysName">
          <el-input
            v-model="queryParams.data[index].value"
            clearable
            size="small"
            style="width: 240px"
          />
        </el-form-item>

      </template>
      <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
    </el-form>

    <!-- 操作 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button

          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button

          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button

          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>

      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <template v-for="field in myDocDefine.fieldList">
        <template v-if="field.isList == '1'">
          <el-table-column :key="'list'+field.key" :label="field.title" :prop="'f.'+field.sysName" :show-overflow-tooltip="true" />

        </template>
      </template>

      <el-table-column label="修改时间" prop="updateTime" width="180" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:config:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['system:config:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <template v-for="(field,index) in form.data">

          <el-form-item :key="'dlg'+field.key" :label="field.title" :prop="field.sysName">
            <el-input v-model="form.data[index].value" placeholder="请输入" />
          </el-form-item>

        </template>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="myCancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getDocConfig } from '@/api/bizDoc/docConfig'
import { getDocAttribute } from '@/api/bizCell/docAttribute'
import { listDoc, getDoc, delDoc, addDoc, updateDoc } from '@/api/bizDoc/dyncDoc'

export default {
  name: 'DocList',
  data() {
    return {

      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 参数表格数据
      dataList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        data: [],
        params: {}

      },
      // 表单参数
      form: {
        data: [],
        id: undefined
      },

      // docPath
      docId: '',
      docConfig: {},
      myDocDefine: {},
      rules: {

      }

    }
  },
  created() {
    this.docId = this.$route.params && this.$route.params.docId
    // console.log('--docConfig---', this.docId)
    this.getData()
  },
  methods: {
    getData() {
      getDocConfig(this.docId).then(response => {
        this.docConfig = response.data
        // console.log('docConfig', this.docConfig)
        this.queryParams.docSysName = this.docConfig.docSysName

        getDocAttribute(this.docConfig.docFieldPath).then(response => {
          this.myDocDefine = response.data
          // console.log('myDocDefine', this.myDocDefine)

          // init query and form dataModel
          for (var item of this.myDocDefine.fieldList) {
            if (item.isQuery === '1') {
              var queryItem = { value: '' }
              queryItem.key = item.key
              queryItem.sysName = item.sysName
              queryItem.title = item.title
              queryItem.queryType = item.queryType
              this.queryParams.data.push(queryItem)
            }
            var editItem = { value: '' }
            editItem.key = item.key
            editItem.sysName = item.sysName
            editItem.title = item.title
            editItem.htmlType = item.type
            this.form.data.push(editItem)
          }

          this.getList()
        })
      })
    },

    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增记录'
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = row.id || this.ids
      this.$confirm('是否确认删除参数编号为"' + configIds + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delDoc(configIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },

    getList() {
      this.loading = true

      for (var item of this.queryParams.data) {
        console.log('queryItem', item)
        this.queryParams.params[item.sysName] = item.value
      }

      listDoc(this.queryParams).then(response => {
        this.dataList = response.rows
        this.total = response.total
        this.loading = false
      }
      )
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      for (var item of this.queryParams.data) {
        item.value = ''
      }
      this.handleQuery()
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const configId = row.id || this.ids
      getDoc(configId).then(response => {
        var dataValue = response.data
        for (var item of this.form.data) {
          item.value = dataValue.f[item.sysName]
        }
        this.form.id = dataValue.id
        this.open = true
        this.title = '修改'
      })
    },

    /** 提交按钮 */
    submitForm: function() {
      if (this.form.id !== undefined) {
        var editData = { f: {}}
        editData.docSysName = this.docConfig.docSysName
        editData.id = this.form.id
        var idx = 0
        for (var item1 of this.form.data) {
          editData.f[item1.sysName] = this.form.data[idx].value
          idx++
        }
        console.log('editData', editData)

        updateDoc(editData).then(response => {
          this.msgSuccess('修改成功')
          this.open = false
          this.getList()
        })
      } else {
        var newData = { f: {}}
        newData.docSysName = this.docConfig.docSysName
        var index = 0
        for (var item of this.form.data) {
          newData.f[item.sysName] = this.form.data[index].value
          index++
        }
        console.log('newData', newData)

        addDoc(newData).then(response => {
          this.msgSuccess('新增成功')
          this.open = false
          this.getList()
        })
      }
    },
    // 取消按钮
    myCancel() {
      this.open = false
      // this.reset()
    },
    // 表单重置
    reset() {
      for (var index in this.myDocDefine.fieldList) {
        this.form.data[index].value = ''
      }
      this.form.id = undefined
    }

  }

}
</script>

<style>

</style>
