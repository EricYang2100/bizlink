<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" label-width="68px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="名称" prop="tpName">
            <el-input
              v-model="queryParams.tpName"
              placeholder="合作伙伴名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="城市" prop="city">
            <el-cascader
              v-model="queryParams.cityOptions"
              placeholder="合作伙伴所在城市"
              :options="cityAllOptions"
              @change="handleChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="类型" prop="typeId">
            <treeselect
              v-model="queryParams.tpTypeId"
              :options="categoryOptions"
              :normalizer="normalizer"
              placeholder="请选择类型"
              :show-count="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="部门" prop="deptId">
            <treeselect
              v-model="queryParams.deptId"
              :options="deptOptions"
              :show-count="true"
              placeholder="请选择归属部门"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6">
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="状态"
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
        </el-col>
        <el-col :span="6">
          <el-form-item label="负责人" prop="status">
            <el-select
              v-model="queryParams.tpMgr"
              placeholder="负责人"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in userDictOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:edit']"
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
          v-hasPermi="['system:config:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:config:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col> -->

      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      >
      <el-table-column label="名称" align="center" prop="tpName" :show-overflow-tooltip="true" />
      <el-table-column label="城市" align="center" prop="city" :formatter="cityFormat" />
      <el-table-column label="类型" align="center" prop="tpTypeId" :formatter="typeFormat" />
      <el-table-column label="部门" align="center" prop="dept" :formatter="deptFormat" />
      <el-table-column label="负责人" align="center" prop="tpMgr" :formatter="userFormat" />
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="连接状态" align="center" prop="inviteStatus" :formatter="inviteStatusFormat" />
      <el-table-column label="更新时间" align="center" prop="updateTime" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.inviteStatus=='0'||scope.row.inviteStatus=='1'"
            size="mini"
            type="text"
            icon="el-icon-link"
            @click="handleInvite(scope.row)"
          >邀请</el-button>

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
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>

          <el-col :span="12">
            <el-form-item label="名称" prop="tpName">
              <el-input v-model="form.tpName" placeholder="请输入合作伙伴名称" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="类型" prop="tpTypeId">
              <treeselect v-model="form.tpTypeId" :options="categoryOptions" :normalizer="normalizer" placeholder="请选择类型id" :disable-branch-nodes="true" :show-count="true" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-cascader
                v-model="form.cityOptions"
                :options="cityEditOption"
                @change="handleChange2"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="地址" prop="detailAddr">
              <el-input v-model="form.detailAddr" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择归属部门" @select="selectDept" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="负责人">
              <el-select v-model="form.tpMgr" multiple placeholder="请选择">
                <el-option
                  v-for="item in userOptions"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                  :disabled="item.status == 1"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
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

          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>

          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-form></el-dialog>
  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from '@/api/bizTP/tpInfo'
import { treeselect, deptDictList } from '@/api/system/dept'
import { listType, dictList } from '@/api/bizTP/tpType'
import { deptSelect, userDictList } from '@/api/system/user'

import { regionDataPlus, regionData, CodeToText } from 'element-china-area-data'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'TPInfo',
  components: {
    Treeselect
  },
  data() {
    return {
      // 省市选择-含全部
      cityAllOptions: regionDataPlus,

      // 省市选择（编辑时使用）
      cityEditOption: regionData,

      // 遮罩层
      loading: true,
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
      infoList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tpName: undefined,
        cityOptions: []
      },
      // 表单参数
      form: {
        cityOptions: [],
        city: ''
      },
      // 表单校验
      rules: {
        tpName: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ]
      },
      // 状态数据字典
      statusOptions: [],

      // 连接状态数据字典
      inviteStatusOptions: [],

      // 合作伙伴分类树选项
      categoryOptions: [],

      // 合作伙伴类型字典
      typeDict: [],

      // 部门树选项
      deptOptions: [],

      // 部门数据字典
      deptDictOptions: [],

      // 用户数据字典
      userDictOptions: [],
      // 编辑用户选项
      userOptions: []

    }
  },
  created() {
    this.getList()

    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })

    this.getDicts('bizTP_connect_status').then(response => {
      this.inviteStatusOptions = response.data
    })

    dictList().then(response => {
      this.typeDict = response.data
    })

    this.getDeptTreeselect()

    this.getTypeTreeselect()

    deptDictList().then(response => {
      this.deptDictOptions = response.data
    })

    userDictList().then(response => {
      this.userDictOptions = response.data
    })
  },
  methods: {
    /** 查询部门下拉树结构 */
    getDeptTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data
      })
    },

    /** 转换TPtype分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.typeName,
        children: node.children
      }
    },
    /** 查询合作伙伴分类下拉树结构 */
    getTypeTreeselect() {
      listType().then(response => {
        this.categoryOptions = []
        const data = { id: 0, typeName: '顶级节点', children: [] }
        data.children = this.handleTree(response.data, 'id', 'parentId')
        this.categoryOptions.push(data)
      })
    },

    // 编辑时修改部门，查询userOptions
    selectDept() {
      // console.log('deptId=', this.form.deptId)
      deptSelect(this.form.deptId).then(response => {
        this.userOptions = response.data
        // console.log('userOptions=', this.userOptions)
      })
    },
    // 查询条件城市编码与与名称转换
    handleChange(value) {
      this.queryParams.city = ''
      for (let i = 0; i < this.queryParams.cityOptions.length; i++) {
        var s = CodeToText[this.queryParams.cityOptions[i]]
        if (s !== '全部') { this.queryParams.city = this.queryParams.city + s + '/' }
        //   console.log('this.queryParams.city', this.queryParams.city)
      }
      console.log(this.queryParams.city)
    },
    // 编辑对话框城市编码与与名称转换
    handleChange2(value) {
      if (this.form.cityOptions) {
        this.form.city = ''
        for (let i = 0; i < this.form.cityOptions.length; i++) {
          var s = CodeToText[this.form.cityOptions[i]]
          this.form.city = this.form.city + s + '/'
        }
        console.log(this.form.city)
      }
    },

    /** 查询参数列表 */
    getList() {
      this.loading = true
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows
        this.total = response.total
        this.loading = false
      }
      )
    },

    // 字典状态字典翻译
    cityFormat(row, column) {
      if (row.city) {
        return row.city.split('/')[0]
      }
      return ''
    },

    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },

    // 连接状态字典翻译
    inviteStatusFormat(row, column) {
      return this.selectDictLabel(this.inviteStatusOptions, row.inviteStatus)
    },

    // 合作伙伴分类字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeDict, row.tpTypeId)
    },

    // 部门字典翻译
    deptFormat(row, column) {
      return this.selectDictLabel(this.deptDictOptions, row.deptId)
    },

    // 负责人字典翻译
    userFormat(row, column) {
      if (row.tpMgr) { return this.selectDictLabels(this.userDictOptions, row.tpMgr.join(',')) }
      return ''
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        tpName: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 邀请合作伙伴操作 */
    handleInvite() {

    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getTypeTreeselect()
      this.open = true
      this.title = '新增合作伙伴'
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTypeTreeselect()
      const configId = row.id || this.ids
      getInfo(configId).then(response => {
        this.form = response.data
        this.selectDept()
        this.open = true
        this.title = '修改合作伙伴'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateInfo(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addInfo(this.form).then(response => {
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
      const configIds = row.id || this.ids
      this.$confirm('是否确认删除编号为"' + configIds + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delInfo(configIds)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    }
    /** 导出按钮操作 */
    // handleExport() {
    //   const queryParams = this.queryParams
    //   this.$confirm('是否确认导出所有参数数据项?', '警告', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(function() {
    //     return exportConfig(queryParams)
    //   }).then(response => {
    //     this.download(response.msg)
    //   })
    // },

  }
}
</script>
