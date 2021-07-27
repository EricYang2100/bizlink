<template>
  <div class="app-container">
    <!-- 文档名称 -->
    <div>
      <h3>{{ docName }} </h3>
    </div>

    <!-- 查询条件 -->
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <template v-for="field in myDocDefine.fieldList">
        <template v-if="field.isQuery == '1'">

          <el-form-item :key="'query'+field.key" :label="field.title" :prop="field.sysName">
            <el-input
              :v-model=" 'queryParams.'+field.sysName "
              clearable
              size="small"
              style="width: 240px"
            />
          </el-form-item>
        </template>
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
          <el-table-column :key="'list'+field.key" :label="field.title" :prop="field.sysName" :show-overflow-tooltip="true" />

        </template>
      </template>

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

        <template v-for="field in myDocDefine.fieldList">

          <el-form-item :key="'dlg'+field.key" :label="field.title" :prop="field.sysName">
            <el-input :v-model="'form.'+field.sysName" placeholder="请输入" />
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
import { docDefine } from './FieldAttrubuteData'

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
        pageSize: 10

      },
      // 表单参数
      form: {},

      // docPath
      docPath: '',
      docName: '组织机构',
      myDocDefine: docDefine,
      rules: {
        configName: [
          { required: true, message: '参数名称不能为空', trigger: 'blur' }
        ],
        configKey: [
          { required: true, message: '参数键名不能为空', trigger: 'blur' }
        ],
        configValue: [
          { required: true, message: '参数键值不能为空', trigger: 'blur' }
        ]
      }

    }
  },
  created() {

  },
  methods: {
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加参数'
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = row.configId || this.ids
      this.$confirm('是否确认删除参数编号为"' + configIds + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        // return delConfig(configIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },

    getList() {
      this.loading = false
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
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
    //   const configId = row.configId || this.ids
    //   getConfig(configId).then(response => {
    //     this.form = response.data
    //     this.open = true
    //     this.title = '修改参数'
    //   })
    },

    /** 提交按钮 */
    submitForm: function() {

    },
    // 取消按钮
    myCancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
      // configId: undefined,
      // configName: undefined,
      // configKey: undefined,
      // configValue: undefined,
      // configType: 'Y',
      // remark: undefined
      }
      this.resetForm('form')
    }

  }

}
</script>

<style>

</style>
