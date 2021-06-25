import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import ParentView from '@/components/ParentView';

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        hidden: false,
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(\\d+)',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  },
 

 

{
    path: '/appdoc/',
    component: Layout,
    hidden: false,
    meta: { title: '商品管理', icon: '' },
    children: [
      {
        path: 'saleGoods/category/index',
        component: (resolve) => require(['@/views/appdoc/saleGoods/category/index'], resolve),
        name: 'category',
        meta: { title: '商品分类', icon: '' }
        
      },
      {
        path: 'saleGoods/goods/index',
        component: (resolve) => require(['@/views/appdoc/saleGoods/productItem/index'], resolve),
        name: 'productItem',
        meta: { title: '商品列表', icon: '' }
        
      }
    ]
  },

 


  {
    path: '/bizdoc',
    component: Layout,
    hidden: false,
    meta: { title: '文档定义', icon: '' },
    children: [
      {
        path: 'docTreeItem/index',
        component: (resolve) => require(['@/views/bizdoc/docTreeItem/index'], resolve),
        name: 'docTreeItem',
        meta: { title: '定义树', icon: '' }
        
      },
      {
        path: 'docTreeItem/field/:docPath',
        component: (resolve) => require(['@/views/bizdoc/docTreeItem/field'], resolve),
        name: 'field',
        hidden:true,
        meta: { title: '文档字段定义', icon: '' }
        
      },
      {
        path: 'dyncDoc/index',
        component: (resolve) => require(['@/views/bizdoc/dyncDoc/index'], resolve),
        name: 'dyncDoc',
        meta: { title: '动态属性测试', icon: '' }
        
      }
    ]
  },

  

  {
    path: '/system',
    component: Layout,
    hidden: false,
    meta: { title: '系统管理', icon: '' },
    children: [
      {
        path: 'user/index',
        component: (resolve) => require(['@/views/system/user/index'], resolve),
        hidden:false,
        name: 'type',
        meta: { title: '用户管理', icon: '' }
      },
      {
        path: 'role/index',
        component: (resolve) => require(['@/views/system/role/index'], resolve),
        hidden:false,
        name: 'type',
        meta: { title: '角色管理', icon: '' }
      },
      {
        path: 'menu/index',
        component: (resolve) => require(['@/views/system/menu/index'], resolve),
        name: 'menu',
        meta: { title: '菜单管理', icon: '' }
        
      },
      {
        path: 'dept/index',
        component: (resolve) => require(['@/views/system/dept/index'], resolve),
        name: 'menu',
        meta: { title: '部门管理', icon: '' }
        
      },
      {
        path: 'post/index',
        component: (resolve) => require(['@/views/system/post/index'], resolve),
        name: 'post',
        meta: { title: '岗位管理', icon: '' }
        
      },

      {
        path: 'dict/type',
        component: (resolve) => require(['@/views/system/dict/index'], resolve),
        hidden:false,
        name: 'type',
        meta: { title: '数据字典', icon: '' }
      },
      {
        path: 'dict/type/data/:dictId',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        hidden:true,
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      },
      {
        path: 'config/index',
        component: (resolve) => require(['@/views/system/config/index'], resolve),
        name: 'config',
        meta: { title: '参数配置', icon: '' }
        
      },
     
     
      {
        path: 'notice/index',
        component: (resolve) => require(['@/views/system/notice/index'], resolve),
        name: 'notice',
        meta: { title: '通知公告', icon: '' }
        
      }
    ]
  },
  
  

 

  {
    path: '/monitor',
    component: Layout,
    hidden: false,
    meta: { title: '系统监控', icon: '' },
    children: [
      {
        path: 'server/index',
        component: (resolve) => require(['@/views/monitor/server/index'], resolve),
        name: 'server',
        meta: { title: '服务监控', icon: '' }
        
      },
      {
        path: 'cache/index',
        component: (resolve) => require(['@/views/monitor/cache/index'], resolve),
        name: 'cache',
        meta: { title: '缓存监控', icon: '' }
        
      },
      {
        path: 'online/index',
        component: (resolve) => require(['@/views/monitor/online/index'], resolve),
        name: 'online',
        meta: { title: '在线用户', icon: '' }
        
      },
      {
        path: 'operlog/index',
        component: (resolve) => require(['@/views/monitor/operlog/index'], resolve),
        name: 'operlog',
        meta: { title: '操作日志', icon: '' }
        
      },
      {
        path: 'logininfor/index',
        component: (resolve) => require(['@/views/monitor/logininfor/index'], resolve),
        name: 'logininfor',
        meta: { title: '登录日志', icon: '' }
        
      }
     
    ]
  }

  




]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
