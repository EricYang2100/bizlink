<template>
  <div id="organizingStaff" class="flex-box">
    <div class="remark">
      <span>
        tips: <i class="el-icon-remove" />删除节点，
        <i class="el-icon-circle-plus" />添加节点，
        <i class="el-icon-top" />上移， <i class="el-icon-bottom" />下移，
        <i class="el-icon-user-solid" />用户
      </span>
    </div>
    <div style="width: 100%" class="flex-column">
      <el-tree
        ref="menutree"
        class="tree"
        :data="treeData"
        highlight-current-row
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span>
            <el-button type="text" size="mini" @click="remove(node, data)">
              <i class="el-icon-remove" />
            </el-button>
            <el-button
              v-if="data.type == 'BM'"
              type="text"
              size="mini"
              @click="editNode(data)"
            >
              <i class="el-icon-circle-plus" />
            </el-button>
            <el-button
              type="text"
              size="mini"
              @click="menuMoveF(node, data, 'up')"
            >
              <i class="el-icon-top" />
            </el-button>
            <el-button
              type="text"
              size="mini"
              @click="menuMoveF(node, data, 'down')"
            >
              <i class="el-icon-bottom" />
            </el-button>
            <el-button
              v-if="data.type == 'BM'"
              type="text"
              size="mini"
              @click="choseUser(data)"
            >
              <i class="el-icon-user-solid" />
            </el-button>
          </span>
        </span>
      </el-tree>
    </div>
    <el-dialog
      title="新建节点"
      :visible.sync="nodeDialog"
      width="50%"
      append-to-body
    >
      <el-form ref="form" label-width="80px" size="mini">
        <el-form-item label="节点名称：" label-width="100px">
          <el-input v-model="nodeName" placeholder="请输入节点名称" />
        </el-form-item>
        <el-form-item
          label-width="40px"
          style="float: right; margin-top: -10px"
        >
          <el-button
            type="primary"
            round
            icon="el-icon-circle-check"
            @click="append(nodeData)"
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
    <el-dialog
      title="选择用户"
      :visible.sync="userDialog"
      width="60%"
      append-to-body
    >
      <el-form
        ref="form"
        inline
        :model="userForm"
        label-width="80px"
        size="mini"
      >
        <el-form-item label="姓名：" label-width="100px">
          <el-input
            v-model="userForm.userName"
            style="width: 150%"
            placeholder="请输入姓名"
          />
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" round icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="userTable"
        :data="userTable"
        border
        style="width: 100%"
        size="small"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" />
        <el-table-column prop="userName" label="姓名" />
        <el-table-column prop="dept" label="部门" />
      </el-table>
      <el-form size="mini" style="float: right">
        <el-form-item label-width="40px">
          <el-button
            type="primary"
            round
            icon="el-icon-circle-check"
            @click="editUser(userNodeData)"
          >确定</el-button>
          <el-button
            type="primary"
            round
            icon="el-icon-delete"
            @click="userDialog = false"
          >取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import CircularJSON from 'circular-json'
let id = 1000
export default {
  name: 'OrganizingStaff',
  data() {
    return {
      userSelection: [],
      userTable: [
        {
          userName: '张三',
          dept: '综合管理部'
        },
        {
          userName: '李四',
          dept: '综合管理部'
        },
        {
          userName: '王五',
          dept: '综合管理部'
        }
      ],
      userForm: {},
      userDialog: false,
      nodeDialog: false,
      nodeName: '',
      nodeData: undefined,
      userNodeData: undefined,
      treeData: [
        {
          id: 1,
          label: '公司领导',
          type: 'BM',
          children: [
            {
              id: 2,
              label: '二级 1-1',
              type: 'BM',
              children: [
                {
                  id: 3,
                  label: '三级 1-1-1',
                  type: 'RY'
                }
              ]
            }
          ]
        },
        {
          id: 4,
          label: '公司中层',
          type: 'BM',
          children: [
            {
              id: 5,
              label: '二级 2-1',
              type: 'RY'
            }
          ]
        }
      ]
    }
  },
  watch: {},
  created() {},
  methods: {
    // 添加节点
    append(data) {
      if (
        this.nodeName === undefined ||
        this.nodeName === '' ||
        this.nodeName == null
      ) {
        this.$message.warning('请输入节点名称')
        return
      }
      const newChild = {
        id: id++,
        label: this.nodeName,
        type: 'BM',
        children: []
      }
      if (!data.children) {
        this.$set(data, 'children', [])
      }
      data.children.push(newChild)
      this.nodeDialog = false
    },
    // 移除节点
    remove(node, data) {
      const parent = node.parent
      const children = parent.data.children || parent.data
      const index = children.findIndex((d) => d.id === data.id)
      children.splice(index, 1)
    },
    editNode(data) {
      this.nodeName = ''
      this.nodeDialog = true
      this.nodeData = data
    },
    // 移动点击函数(node：当前节点，data：当前节点的data，type：区分上下移动)
    menuMoveF(node, data, type) {
      // 将变动之前的node备份
      const copyNode = { ...node }
      copyNode.previousSibling = { ...node.previousSibling }
      copyNode.nextSibling = { ...node.nextSibling }
      window.sessionStorage.setItem(
        'menuNode',
        CircularJSON.stringify(copyNode)
      )
      let nodeData = {}
      if (type === 'up') {
        // 上移
        if (node.previousSibling) {
          // 删除原先的node
          this.$refs.menutree.remove(node.data)
          // 拿到copy的node
          nodeData = CircularJSON.parse(
            window.sessionStorage.getItem('menuNode')
          )
          // 复制该node到指定位置(参数：1. 要增加的节点的 data 2. 要增加的节点的后一个节点的 data、key 或者 node)
          this.$refs.menutree.insertBefore(
            nodeData.data,
            nodeData.previousSibling.data
          )
          window.sessionStorage.removeItem('menuNode')
        } else {
          this.$message.warning('该菜单已经是当前层最上级')
        }
      } else {
        // 下移
        if (node.nextSibling) {
          this.$refs.menutree.remove(node.data)
          nodeData = CircularJSON.parse(
            window.sessionStorage.getItem('menuNode')
          )
          // 参数：1. 要增加的节点的 data 2. 要增加的节点的前一个节点的 data、key 或者 node
          this.$refs.menutree.insertAfter(
            nodeData.data,
            nodeData.nextSibling.data
          )
          window.sessionStorage.removeItem('menuNode')
        } else {
          this.$message.warning('该菜单已经是当前层最下级')
        }
      }
    },
    choseUser(data) {
      this.userNodeData = data
      this.userDialog = true
      this.userSelection = []
      this.$refs.userTable.clearSelection()
    },
    // 选择的用户
    handleSelectionChange(val) {
      this.userSelection = val
    },
    editUser(data) {
      this.userSelection.forEach((item) => {
        const newChild = {
          id: id++,
          label: item.userName,
          type: 'RY'
        }
        data.children.push(newChild)
      })
      this.userDialog = false
    }
  }
}
</script>

<style scoped>
.flex-box {
  display: flex;
  position: relative;
  flex-direction: column;
  height: 100%;
}
.flex-column {
  height: 100%;
}
.tree {
  position: relative;
  width: 500px;
  margin: 0.5rem 0 0 5rem;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  padding-right: 8px;
}
.remark {
  margin: 1rem 0 0 6rem;
}
.remark >>> i {
  color: #40a0ff;
}
</style>

