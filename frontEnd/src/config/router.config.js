// eslint-disable-next-line
import { UserLayout, BasicLayout } from '@/layouts'
import { bxAnaalyse } from '@/core/icons'
import { RouteView } from '@/layouts/index'

export const asyncRouterMap = [

  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/reimbursement/travel',
    children: [
      {
          path: '/reimbursement',
          name: 'reimbursement',
          meta: { title: '报销', keepAlive: true, icon: bxAnaalyse },
          component: RouteView,
          redirect: '/reimbursement/travel',
          children: [
            {
                path: '/reimbursement/travel',
                name: 'travel',
                meta: { title: '出差报销', keepAlive: true },
                component: () => import('@/views/travel')
              },
            {
                path: '/reimbursement/fertility',
                name: 'fertility',
                meta: { title: '生育报销', keepAlive: true },
                component: () => import('@/views/feitility')
              },
              {
                path: '/reimbursement/union',
                name: 'union',
                meta: { title: '工会报销', keepAlive: true },
                component: () => import('@/views/union')
              }
          ]
      }
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      },
      {
        path: 'recover',
        name: 'recover',
        component: undefined
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }

]
