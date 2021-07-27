<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px">

      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.f.name"
          placeholder="请输入系统名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="动态属性d1" prop="d1">
        <el-input
          v-model="queryParams.f.d1"
          placeholder="请输入动态属性"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
      :data="docList"
      row-key="id"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >

      <el-table-column label="名称" prop="f.name" />

      <el-table-column label="动态属性1" prop="f.d1" />
      <el-table-column label="动态属性2" prop="f.d2" />
      <el-table-column label="动态属性3" prop="f.d3" />
      <el-table-column label="动态属性4" prop="f.d4" />
      <el-table-column label="动态属性5" prop="f.d5" />
      <el-table-column label="修改时间" prop="updateTime" />

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
            v-hasPermi="['bizdoc:category:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>

        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改商品分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="名称" prop="name">
          <el-input v-model="form.f.name" placeholder="中文名称" />
        </el-form-item>

        <el-form-item label="d1" prop="d1">
          <el-input v-model="form.f.d1" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="d2" prop="d2">
          <el-input v-model="form.f.d2" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="d3" prop="d3">
          <el-input-number v-model="form.f.d3" controls-position="right" :min="0" />
        </el-form-item>

        <el-form-item label="d4" prop="d4">
          <el-input v-model="form.f.d4" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="d5" prop="d5">
          <el-input v-model="form.f.d5" placeholder="请输入内容" />
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
import { listDoc, getDoc, delDoc, addDoc, updateDoc } from '@/api/bizDoc/dyncDoc'

export default {
  name: 'MyDyncDoc',

  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 商品分类表格数据
      docList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        f: {}

      },

      // 表单参数
      form: {
        f: {}
      },
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询商品分类列表 */
    getList() {
      this.loading = true
      listDoc(this.queryParams).then(response => {
        this.docList = response.data

        this.loading = false
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
        f: {},
        createTime: null,
        updateTime: null
      }
      this.resetForm('form')
    },

    // 表单重置
    resetQueryForm() {
      this.queryParams = {

        f: {}

      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.resetQueryForm()

      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()

      this.open = true
      this.title = '添加节点'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()

      getDoc(row.id).then(response => {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除编号为"' + row.id + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delDoc(row.id)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    }
  }

}
</script>
