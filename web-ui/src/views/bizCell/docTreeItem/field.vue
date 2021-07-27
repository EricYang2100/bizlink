<template>
  <div class="app-container">

    <div style="margin-bottom:16px">

      文档：{{ docTreeItem.docPath }}({{ docTreeItem.name }})
    </div>
    <!-- <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">

      <el-form-item label="系统名称" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入系统名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="中文名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入中文名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="类型" prop="docType">
        <el-select
          v-model="queryParams.docType"
          placeholder="类型"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in fieldTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="分类状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['bizdoc:category:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <right-toolbar @queryTable="getList" />
    </el-row>

    <el-table
      v-loading="loading"
      :data="fieldList"
      row-key="id"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >

      <el-table-column label="系统名称" prop="sysName" />
      <el-table-column label="中文名称" prop="fieldName" />
      <el-table-column label="类型" align="center" prop="columnType" :formatter="typeFormat" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="顺序" prop="orderNum" />
      <el-table-column label="系统缺省" align="center" prop="sysDefault" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['bizdoc:category:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['bizdoc:category:add']"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >新增</el-button>
          <el-button
            v-hasPermi="['bizdoc:category:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          <template v-if="scope.row.docType==='DOC-SHARE'">
            <el-dropdown @command="handleCommand">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right" />
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="composeValue('field',scope.row)">字段设置</el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改商品分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col span="12">
            <el-form-item label="上级节点" prop="parentId">
              <treeselect v-model="form.parentId" :options="treeItemOptions" :normalizer="normalizer" placeholder="请选择父分类id" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>

          <el-col span="12">
            <el-form-item label="系统名称" prop="sysName">
              <el-input v-model="form.sysName" placeholder="系统名称" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="中文名称" prop="fieldName">
              <el-input v-model="form.fieldName" placeholder="中文名称" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="数据类型" prop="columnType">
              <el-select
                v-model="form.columnType"
                placeholder="类型"
                size="small"
                style="width: 240px"
              >
                <el-option
                  v-for="dict in fieldTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="字段属性">
              <el-checkbox v-model="form.isRequired" true-label="1" label="必选" />
              <el-checkbox v-model="form.isList" true-label="1" label="列表" />
              <el-checkbox v-model="form.isQuery" true-label="1" label="查询" />
            </el-form-item>
          </el-col>

          <el-col span="12">
            <el-form-item v-if="form.isQuery == '1'" label="查询方式" prop="queryType">
              <el-select v-model="form.queryType">
                <el-option label="=" value="EQ" />
                <el-option label="!=" value="NE" />
                <el-option label=">" value="GT" />
                <el-option label=">=" value="GTE" />
                <el-option label="<" value="LT" />
                <el-option label="<=" value="LTE" />
                <el-option label="LIKE" value="LIKE" />
                <el-option label="REGEX" value="REGEX" />
                <el-option label="BETWEEN" value="BETWEEN" />
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col span="12">
            <el-form-item label="显示类型" prop="htmlType">
              <el-select v-model="form.htmlType">
                <el-option label="文本框" value="input" />
                <el-option v-if="form.columnType=='Long'" label="数字" value="input-number" />
                <el-option v-if="form.columnType=='String'" label="下拉选择" value="select" />
                <el-option v-if="form.columnType=='String'" label="字典选择" value="dictSelect" />
                <el-option v-if="form.columnType=='String'" label="弹窗选择" value="dlgSelect" />
                <el-option v-if="form.columnType=='String'" label="树状选择" value="treeSelect" />
                <el-option v-if="form.columnType=='String'" label="单选框" value="radiobox" />
                <el-option v-if="form.columnType=='String'" label="复选框" value="checkhox" />
                <el-option v-if="columnType=='Date'" label="日期控件" value="Date" />
                <el-option v-if="columnType=='String'" label="图片" value="image" />
                <el-option v-if="columnType=='String'" label="附件" value="file" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col span="12">
            <el-form-item v-if="form.htmlType == 'dictSelect'" label="数据字典" prop="dictType">
              <el-select v-model="form.dictType">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.id"
                  :label="item.dictName"
                  :value="item.dictType"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <!--
            todo：下拉列表选择内容
            <el-col span="12">
            <el-form-item v-if="form.htmlType == 'select'" label="选择项" prop="optionList">
              <el-select v-model="form.dictType">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.id"
                  :label="item.dictName"
                  :value="item.dictType"
                />
              </el-select>
            </el-form-item>
          </el-col> -->

        </el-row>

        <el-row>
          <el-col span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>

        </el-row></el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listField, getField, delField, addField, updateField } from '@/api/bizDoc/docField'
import { getTreeItemByPath } from '@/api/bizCell/docTreeItem'
import { optionselect } from '@/api/system/dict/type'

import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'DocTreeItem',
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 文档定义节点
      docTreeItem: {},
      // 表格数据
      fieldList: [],
      // 分类树选项
      treeItemOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        docPath: null,
        sysName: null,
        fieldName: null,
        columnType: null,
        orderNum: null,
        status: null
      },

      // 状态数据字典
      statusOptions: [],

      // 字段类型数据字典
      fieldTypeOptions: [],

      // 类型数据字典
      typeOptions: [],

      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    const docPath = this.$route.params && this.$route.params.docPath
    console.log('--docPath---', docPath)

    this.getDocTreeitem(docPath)
    console.log('--docTreeItem---', this.docTreeItem)

    this.queryParams.docPath = docPath

    this.getList()

    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
    // todo: 设置字段类型字典项并调整
    this.getDicts('sys_field_type').then(response => {
      this.fieldTypeOptions = response.data
    })

    this.getTypeList()
  },
  methods: {

    /** 查询字典类型列表 */
    getTypeList() {
      optionselect().then(response => {
        this.typeOptions = response.data
      })
    },

    /** 查询商品分类列表 */
    getList() {
      this.loading = true
      listField(this.queryParams).then(response => {
        this.fieldList = this.handleTree(response.data, 'id', 'parentId')
        this.loading = false
      })
    },

    getDocTreeitem(docPath) {
      getTreeItemByPath(docPath).then(response => {
        this.docTreeItem = response.data
      })
    },

    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },

    // 字典状态字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.fieldTypeOptions, row.columnType)
    },
    /** 转换item数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.sysName,
        children: node.children
      }
    },
    /** 查询treeItem下拉树结构 */
    getTreeselect() {
      listField().then(response => {
        this.treeItemOptions = []
        const data = { id: 0, sysName: '顶级节点', children: [] }
        data.children = this.handleTree(response.data, 'id', 'parentId')
        this.treeItemOptions.push(data)
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        parentId: null,
        sysName: null,
        fieldName: null,
        columnType: null,
        remark: null,
        orderNum: null,
        status: '0',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.getTreeselect()
      if (row != null && row.id) {
        this.form.parentId = row.id
      } else {
        this.form.parentId = 0
      }
      this.open = true
      this.title = '添加节点'
      this.form.docPath = this.queryParams.docPath
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      if (row != null) {
        this.form.parentId = row.id
      }
      getField(row.id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改节点'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateField(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addField(this.form).then(response => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除商品分类编号为"' + row.id + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delField(row.id)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    }
  }

}
</script>
