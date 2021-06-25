<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入编码"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['bizdoc:category:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="docList"
      row-key="id"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
    
  
      <el-table-column label="名称"  prop="name" />   
      
      <el-table-column label="编码"  prop="code"   />     
      <el-table-column label="规格"  prop="spec"   />  
      <el-table-column label="分类"  prop="category.categoryName"   />  
      <el-table-column label="状态"  prop="status"   :formatter="statusFormat" />  
     
      <el-table-column label="修改时间"  prop="updateTime"   />  
      
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bizdoc:category:edit']"
          >修改</el-button>
          
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bizdoc:category:remove']"
          >删除</el-button>
      
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改商品分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        
     

         <el-form-item label="分类" prop="categoryId">
          <treeselect v-model="form.categoryId" :options="categoryOptions" :normalizer="normalizer" placeholder="请选择分类" />
        </el-form-item>
     
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="名称" />
        </el-form-item>
     

      
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code"  placeholder="请输入编码" />
        </el-form-item>
        
        <el-form-item label="规格" prop="spec">
          <el-input v-model="form.spec"  placeholder="请输入规格" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        

         <el-form-item label="单位" prop="orderNum">
           <el-input v-model="form.uom"  placeholder="请输入内容" />
        </el-form-item>

         <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
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
import { listItem, getItem, delItem, addItem, updateItem, exportDoc } from "@/api/bizdoc/productItem";
import { listCategory, } from "@/api/bizdoc/category";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "productItem",
   components: {
    Treeselect
  },  
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 商品表格数据
      docList: [],
      // 商品分类树选项
      categoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        f:{},  
       
       
      },

      // 状态数据字典
      statusOptions: [],  

      // 表单参数
      form: {
          f:{}
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  
     this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  
  },
  methods: {
    /** 查询商品分类列表 */
    getList() {
      this.loading = true;
      listItem(this.queryParams).then(response => {
        this.docList = response.data;
        
        this.loading = false;
      });
    },

    /** 转换商品分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.categoryName,
        children: node.children
      };
    },
	/** 查询商品分类下拉树结构 */
    getTreeselect() {
      listCategory().then(response => {
        this.categoryOptions = [];
        const data = { id: 0, categoryName: '顶级节点', children: [] };
        data.children = this.handleTree(response.data, "id", "parentId");
        this.categoryOptions.push(data);
      });
    },

    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
	
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,       
        df:{},  
        createTime: null,        
        updateTime: null
      };
      this.resetForm("form");
    },

    // 表单重置
    resetQueryForm() {
      this.queryParams = {
             
        df:{},  
       
      };
     
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.resetQueryForm();
     
      this.handleQuery();
    },
     /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
     this.getTreeselect();
      this.open = true;
      this.title = "添加节点";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getItem(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改节点";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateItem(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addItem(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除编号为"' + row.id + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delItem(row.id);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    }
  },

   
};
</script>
