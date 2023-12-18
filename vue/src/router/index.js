import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        { path: 'home', name: 'Home', component: () => import('@/views/manager/Home.vue')},
        { path: 'course', name: 'Course', component: () => import('@/views/manager/Course.vue')},
        { path: '/teacher', name: 'teacher', component: () => import('@/views/manager/Teacher.vue')},
        { path: '/student', name: 'student', component: () => import('@/views/manager/Student.vue')},
        { path: '/person', name: 'Person', component: () => import('@/views/manager/Person.vue')},
        { path: '/personTeacher', name: 'PersonTeacher', component: () => import('@/views/manager/PersonTeacher.vue')},
        { path: '/courseList', name: 'CourseList', component: () => import('@/views/manager/CourseList.vue')},
        { path: '/exam', name: 'Exam', component: () => import('@/views/manager/Exam.vue')},
        { path: '/grade', name: 'Grade', component: () => import('@/views/manager/Grade.vue')},
        { path: '/examManager', name: 'ExamManager', component: () => import('@/views/manager/ExamManager.vue')},
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login.vue'),
    }
  ]
})

export default router
