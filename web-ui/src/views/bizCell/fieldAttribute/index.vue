<template>

  <div class="mybody">

    <div style="margin-bottom:8px">
      <el-card :body-style="{ padding: '16px' }">
        文档：{{ editData.docPath }}
        <div style="float: right; margin-top:-8px">
          <el-button size="mini" icon="el-icon-refresh" @click="getData">刷新</el-button>
        </div>
      </el-card>

    </div>

    <el-row :gutter="8">
      <el-col :span="12">
        <el-card :body-style="{ padding: '8px' }">
          <div slot="header" class="clearfix">
            <span>字段列表</span>

            <div style="float: right; margin-top:-8px">
              <el-button type="text" icon="el-icon-plus" @click="handleAdd">新增节点</el-button>
              <el-button type="text" icon="el-icon-circle-plus-outline" :disabled="selectRow && !selectRow.children" @click="handleAddChild">
                新增子节点
              </el-button>
              <el-button type="text" icon="el-icon-delete" :disabled="selectRow == null" @click="handleDel">
                删除
              </el-button>
              <el-button type="text" title="上移" icon="el-icon-arrow-up" :disabled="selectRow == null" @click="handleUp">
                上移
              </el-button>
              <el-button type="text" title="下移" icon="el-icon-arrow-down" :disabled="selectRow == null" @click="handleDown">
                下移
              </el-button>

            </div>

          </div>
          <div>
            <el-table
              ref="myTable"
              :data="editData.fieldList"
              :row-class-name="tableRowClassName"
              style="width: 100%;margin-bottom: 20px;border: 1px solid #1890ff"
              row-key="key"
              default-expand-all
              highlight-current-row
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
              @current-change="currentChange"
            >
              <el-table-column prop="title" label="显示名称" />
              <el-table-column prop="sysName" label="系统名称" />
              <el-table-column prop="dataType" label="类型" :formatter="typeFormat" />

            </el-table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card :body-style="{ padding: '8px' }">
          <div slot="header" class="clearfix">
            <span>字段属性</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleEdit">
              编辑
            </el-button>
          </div>

          <div v-if="selectRow">
            <el-form ref="form" :model="selectRow" label-width="100px" class="demo-table-expand">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="系统名称">
                    <span> {{ selectRow.sysName }}</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="显示名称">
                    <span> {{ selectRow.title }}</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="数据类型">
                    <span> {{ selectDictLabel(fieldTypeOptions, selectRow.dataType) }}</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="必选字段">
                    <span v-if="selectRow.isRequired=='1'">是</span>
                    <span v-else>否</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="列表字段">
                    <span v-if="selectRow.isList=='1'">是</span>
                    <span v-else>否</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="查询字段">
                    <span v-if="selectRow.isQuery=='1'">是</span>
                    <span v-else>否</span>
                  </el-form-item>
                </el-col>

                <el-col v-if="selectRow.isQuery == '1'" :span="12">
                  <el-form-item label="查询方式" prop="queryType">
                    <span> {{ selectDictLabel(queryOptions, selectRow.queryType) }}</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="显示类型" prop="htmlType">
                    <span> {{ selectDictLabel(htmlTypeOptions, selectRow.htmlType) }}</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="数据字典" prop="dictType">
                    <span>type名称</span>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="内容可选项" prop="dictType">
                    <span>未设置</span>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="备注" prop="remark">
                    <span v-if="selectRow.remark!==null">{{ selectRow.remark }}</span>
                    <span v-else>未设置</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>

          </div>
          <div v-else>
            请在左边选择对应字段
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 新增对话框 -->
    <el-dialog
      title="新建节点"
      :visible.sync="nodeDialog"
      width="40%"
      append-to-body
    >
      <el-form ref="form" label-width="100px" size="mini">
        <el-form-item label="系统名称：">
          <el-input v-model="newNode.sysName" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="显示名称：">
          <el-input v-model="newNode.title" placeholder="请输入显示名称" />
        </el-form-item>
        <el-form-item label="数据类型：" prop="dataType">
          <el-select
            v-model="newNode.dataType"
            placeholder="选择数据类型"
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
        <el-form-item
          label-width="40px"
          style="float: right; margin-top: -10px"
        >
          <el-button
            type="primary"
            round
            icon="el-icon-circle-check"
            @click="append(newNode)"
          >确定</el-button>
          <el-button
            type="primary"
            round
            icon="el-icon-delete"
            @click="nodeDialog = false"
          >取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="sysName">
              <el-input v-model="form.sysName" placeholder="系统名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="中文名称" prop="fieldName">
              <el-input v-model="form.title" placeholder="中文名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据类型" prop="columnType">
              <el-select
                v-model="form.dataType"
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

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="字段属性">
              <el-checkbox v-model="form.isRequired" true-label="1" label="必选" />
              <el-checkbox v-model="form.isList" true-label="1" label="列表" />
              <el-checkbox v-model="form.isQuery" true-label="1" label="查询" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item v-if="form.isQuery == '1'" label="查询方式" prop="queryType">

              <el-select v-model="form.queryType">
                <el-option
                  v-for="dict in queryOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示类型" prop="htmlType">
              <el-select v-model="form.htmlType">
                <el-option
                  v-for="dict in htmlTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
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

        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>

        </el-row></el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open=false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import { getDocAttribute, updateDocAttribute } from '@/api/bizCell/docAttribute'

export default {

  data() {
    return {

      // 编辑对话框弹出层标题
      title: '',

      // 编辑对话框是否显示弹出层
      open: false,

      // 新增对话看是否显示
      nodeDialog: false,

      // 新增节点缺省值
      newNode: {
        parent: null,
        sysName: '',
        title: '',
        dataType: 'String'
      },

      editData: {
        id: '',
        docPath: '',
        maxId: 0,
        fieldList: []
      },
      docPath: '',
      form: {
        sysName: '',
        title: '',
        dataType: '',
        editType: '',
        isRequest: '0',
        isList: '0',
        isQuery: '0',
        queryType: '=',
        htmlType: '',
        dictSetting: '',
        dictType: '',
        chooseValue: []
      },
      // 字段类型数据字典
      fieldTypeOptions: [],

      // 查询方法数据字典
      queryOptions: [],

      // 编辑数据字典
      htmlTypeOptions: [],

      selectRow: null
    }
  },

  created() {
    this.docPath = this.$route.params && this.$route.params.docPath
    console.log('--docPath---', this.docPath)
    this.getData()

    this.getDicts('bizCell_field_type').then(response => {
      this.fieldTypeOptions = response.data
    })

    this.getDicts('bizCell_field_htmlType').then(response => {
      this.htmlTypeOptions = response.data
    })

    this.getDicts('bizCell_field_query').then(response => {
      this.queryOptions = response.data
    })
    // this.selectNode(fieldTreeData[0])
  },

  methods: {
    tableRowClassName({ row, rowIndex }) {
      // 把每一行的索引放进row
      row.index = rowIndex
    },
    getData() {
      this.loading = true
      getDocAttribute(this.docPath).then(response => {
        this.editData = response.data

        this.loading = false
      })
    },

    saveData() {
      updateDocAttribute(this.editData).then(response => {
        this.msgSuccess('修改成功')
      })
    },

    currentChange(currentRow) {
      console.log('currentChange', currentRow)
      this.selectRow = currentRow
    },
    // 字段类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.fieldTypeOptions, row.dataType)
    },

    typeFormat2(dataType) {
      return this.selectDictLabel(this.fieldTypeOptions, dataType)
    },

    handleAdd() {
      var parent
      if (!this.selectRow) {
        parent = this.editData.fieldList
      } else {
        parent = this.getParent(this.editData.fieldList, this.editData.fieldList, this.selectRow)
      }
      this.newNode.parent = parent
      this.nodeDialog = true
    },

    handleAddChild() {
      this.newNode.parent = this.selectRow
      this.nodeDialog = true
    },

    append(newNode) {
      console.log('newNode', newNode)
      const newChild = {
        key: this.editData.maxId++,
        title: newNode.title,
        sysName: newNode.sysName,
        dataType: newNode.dataType,
        children: null

      }

      if (newChild.dataType === 'Record' || newChild.dataType === 'List') {
        newChild.children = []
      }
      console.log('newChild', newChild)
      console.log('parent', newNode.parent)
      const children = newNode.parent.children || newNode.parent
      children.push(newChild)
      this.nodeDialog = false
      this.selectNode(newChild)
      this.saveData()
      this.resetAddForm()
    },

    resetAddForm() {
      this.newNode = {
        parent: null,
        sysName: '',
        title: '',
        dataType: 'String'

      }
    },

    handleEdit() {
      this.form = {
        sysName: this.selectRow.sysName,
        title: this.selectRow.title,
        dataType: this.selectRow.dataType,
        editType: this.selectRow.editType,
        isRequired: this.selectRow.isRequired,
        isList: this.selectRow.isList,
        isQuery: this.selectRow.isQuery,
        queryType: this.selectRow.queryType,
        htmlType: this.selectRow.htmlType,
        dictSetting: this.selectRow.dictSetting,
        dictType: this.selectRow.dictType,
        chooseValue: this.selectRow.chooseValue,
        remark: this.selectRow.remark
      }
      this.open = true
    },

    /** 提交按钮 */
    submitForm() {
    //  console.log('rowIndex', this.selectRow.index)
      this.editData.fieldList[this.selectRow.index] = {
        sysName: this.form.sysName,
        title: this.form.title,
        dataType: this.form.dataType,
        editType: this.form.editType,
        isRequired: this.form.isRequired,
        isList: this.form.isList,
        isQuery: this.form.isQuery,
        queryType: this.form.queryType,
        htmlType: this.form.htmlType,
        dictSetting: this.form.dictSetting,
        dictType: this.form.dictType,
        chooseValue: this.form.chooseValue,
        remark: this.form.remark
      }

      updateDocAttribute(this.editData).then(response => {
        this.msgSuccess('修改成功')
        this.open = false
        this.selectNode(this.editData.fieldList[this.selectRow.index])
      })
    },

    /** 删除按钮操作 */
    handleDel() {
      const sel = this // then can't use this
      this.$confirm('是否确认删除"' + this.selectRow.sysName + '"的字段?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        sel.doRealDel()
        this.saveData()
        sel.msgSuccess('删除成功')
      })
    },

    /** 真正删除实现 */
    doRealDel() {
      // console.log('node', this.selectRow)
      const parent = this.getParent(this.tableData, this.tableData, this.selectRow)
      // console.log('parent', parent)
      const children = parent.children || parent
      const index = children.findIndex((d) => d.key === this.selectRow.key)
      children.splice(index, 1)
      const selNode = children[index] || children[index - 1] || parent
      this.selectNode(selNode)
    },

    /** 设置当前选中行 */
    selectNode(node) {
      this.$nextTick(() => {
        this.$refs.myTable.setCurrentRow(node)
      })
    },

    // 上移当前选中行
    handleUp() {
      const parent = this.getParent(this.tableData, this.tableData, this.selectRow)
      // console.log('parent', parent)
      const children = parent.children || parent
      const index = children.findIndex((d) => d.key === this.selectRow.key)
      if (index === 0) {
        this.msgError('已经是最前面了')
        return
      }
      const prev = children[index - 1]
      children.splice(index - 1, 2, this.selectRow, prev)
      this.saveData()
      this.selectNode(this.selectRow)
    },

    // 下移当前选中行
    handleDown() {
      const parent = this.getParent(this.tableData, this.tableData, this.selectRow)
      // console.log('parent', parent)
      const children = parent.children || parent
      const index = children.findIndex((d) => d.key === this.selectRow.key)
      if (index === (children.length - 1)) {
        this.msgError('已经是最后面了')
        return
      }
      const next = children[index + 1]
      children.splice(index, 2, next, this.selectRow)
      this.saveData()
      this.selectNode(this.selectRow)
    },

    // 获取node节点的父节点， tree 为children数组
    getParent(tree, data, node) {
      // console.log('tree', tree)
      // console.log('node.key', node.key)
      for (const child of tree) {
        // console.log('child', child)
        if (child.key === node.key) {
          return data
        }
        if (child.children) {
          // console.log('children', child.children)
          const res = this.getParent(child.children, child, node)
          if (res) return res
        }
      }
      return null
    }

  }

}
</script>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    /* margin-bottom: 0; */
    width: 50%;
  }
</style>

