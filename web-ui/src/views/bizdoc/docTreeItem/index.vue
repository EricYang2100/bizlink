<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">

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
            v-for="dict in docTypeOptions"
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
    </el-form>

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
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table
      v-loading="loading"
      :data="treeItemList"
      row-key="id"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >

      <el-table-column label="系统名称" prop="code" />
      <el-table-column label="中文名称" prop="name" />

      <el-table-column label="类型" align="center" prop="docType" :formatter="typeFormat" />
      <el-table-column label="备注" align="center" prop="remark" />
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
            <!-- <el-button size="mini" type="text">test</el-button> -->
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
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="目录" prop="parentId">
          <treeselect v-model="form.parentId" :options="treeItemOptions" :normalizer="normalizer" placeholder="请选择父分类id" />
        </el-form-item>
        <el-form-item label="系统名称" prop="code">
          <el-input v-model="form.code" placeholder="系统名称" />
        </el-form-item>
        <el-form-item label="中文名称" prop="name">
          <el-input v-model="form.name" placeholder="中文名称" />
        </el-form-item>

        <el-form-item label="类型" prop="docType">
          <el-select
            v-model="form.docType"
            placeholder="类型"
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="dict in docTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
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
import { listTreeItem, getTreeItem, delTreeItem, addTreeItem, updateTreeItem } from '@/api/bizdoc/docTreeItem'
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
      // 商品分类表格数据
      treeItemList: [],
      // 商品分类树选项
      treeItemOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        parentId: null,
        code: null,
        name: null,
        docType: null,
        orderNum: null,
        status: null
      },

      // 状态数据字典
      statusOptions: [],

      // 文档类型数据字典
      docTypeOptions: [],

      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()

    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_doc_type').then(response => {
      this.docTypeOptions = response.data
    })
  },
  methods: {
    /** 查询商品分类列表 */
    getList() {
      this.loading = true
      listTreeItem(this.queryParams).then(response => {
        this.treeItemList = this.handleTree(response.data, 'id', 'parentId')
        this.loading = false
      })
    },

    // dropItem command 值
    composeValue(item, row) {
      return {
        'button': item,
        'row': row
      }
    },

    // 更多按钮操作处理
    handleCommand(command) {
      console.log(command)
      if (command.button === 'field') {
        // console.log("docPath", command.row.docPath);

        this.$router.push('/bizdoc/docTreeItem/field/' + command.row.docPath)
      }
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },

    // 字典状态字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.docTypeOptions, row.docType)
    },
    /** 转换item数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.code,
        children: node.children
      }
    },
    /** 查询treeItem下拉树结构 */
    getTreeselect() {
      listTreeItem().then(response => {
        this.treeItemOptions = []
        const data = { id: 0, code: '顶级节点', children: [] }
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
        ancestors: null,
        code: null,
        name: null,
        docType: null,
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
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      if (row != null) {
        this.form.parentId = row.id
      }
      getTreeItem(row.id).then(response => {
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
            updateTreeItem(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addTreeItem(this.form).then(response => {
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
        return delTreeItem(row.id)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    }
  }

}
</script>
