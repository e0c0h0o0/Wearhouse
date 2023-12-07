import { createRouter, createWebHistory } from 'vue-router';
const routes=[{
    path:'/',
    name:'login',
    component:()=>import('../components/Login.vue')
},
    {
        path:'/Index',
        name:'index',
        component:()=>import('../components/Index.vue'),
        // redirect: '/Home',
        children:[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'HomePage'
                },
                component:()=>import('../components/Home.vue'),
            },
            {
                path:'/Main',
                name:'Main',
                meta:{
                    title:'Main'
                },
                component:()=>import('../components/Main.vue'),
            },
            {
                path:'/Admin',
                name:'admin',
                meta:{
                    title:'Manger'
                },
                component:()=>import('../components/admin/AdminMange.vue'),
            },
            {
                path:'/User',
                name:'user',
                meta:{
                    title:'User'
                },
                component:()=>import('../components/user/UserMange.vue'),
            }
            ,
            {
                path:'/Storage',
                name:'storage',
                meta:{
                    title:'Storage'
                },
                component:()=>import('../components/storage/StorageManage.vue'),
            }
            ,
            {
                path:'/Goodstype',
                name:'goodstype',
                meta:{
                    title:'goodstype'
                },
                component:()=>import('../components/goodstype/GoodstypeManage.vue'),
            },
            {
                path:'/Goods',
                name:'goods',
                meta:{
                    title:'goods'
                },
                component:()=>import('../components/goods/GoodsManage.vue'),
            },
            {
                path:'/Record',
                name:'record',
                meta:{
                    title:'record'
                },
                component:()=>import('../components/record/RecordManage.vue'),
            }
        ]
    }
]
const router = createRouter({
    history: createWebHistory(),
    routes,
});


export default router;
