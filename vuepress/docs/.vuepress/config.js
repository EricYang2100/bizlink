module.exports = {
    title: '比邻(BizLink)',
    description: 'Just playing around',
    base: '/',
    dest: './docs/.vuepress/dist',
    // 主题配置
     themeConfig: {
        nav: [
            { text: '主页', link: '/' },
            { text: '指南', link: '/guide/' },
            { text: '生活', link: '/life/life01'},
            { text: '学习',
                items: [
                    { text: '英语', link: '/study/english/english01' },
                    { text: '数学', link: '/study/math/math01' },
                ]
            },
            {text: 'Github', link: 'https://github.com/momo-0902'},
			],
        // 为以下路由添加左侧边栏
        sidebar: {
            '/guide/': [
                {
                    title: '指南',
                    collapsable: false,
                    children: [
                        { title: 'page-0', path: '/guide/' },
                        { title: '缘起', path: '/guide/story' },
                        { title: 'page-A', path: '/guide/page-a' },
                        { title: 'page-B', path: '/guide/page-b' },
                       
                    ]
                }
            ],


            '/life/': [
                {
                    title: '生活',
                    collapsable: false,
                    children: [
                        { title: 'Overview', path: '/life/' },
                        { title: '生活测试01', path: '/life/life01' },
                        { title: '生活测试02', path: '/life/life02' },
                        { title: '生活测试03', path: '/life/life03' },
                    ]
                }
            ],


            '/study/english/': [
                {
                    title: '英语',
                    collapsable: false,
                    children: [
                        { title: '第一节', path: '/study/english/english01' },
                        { title: '第二节', path: '/study/english/english02' },
                        { title: '第三节', path: '/study/english/english03' },
                    ]
                }
            ],

            '/study/math/': [
                {
                    title: '数学',
                    collapsable: false,
                    children: [
                        { title: '第一节', path: '/study/math/math01' },
                        { title: '第二节', path: '/study/math/math02' },
                       
                    ]
                }
            ],
        },
         // 这是嵌套标题链接，自动显示当前激活（导航）页面标题的链接，即显示深度（h1-h6的深度）
         sidebarDepth: 2

    }
}

