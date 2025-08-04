import { createRouter, createWebHistory } from 'vue-router'

import InitPage from '../pages/InitPage.vue';
import ItemOverview from '../pages/ItemOverview.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  
  routes: [
    {
      path: '/',
      name: 'initPage',
      component: InitPage,
    },
    {
      path: '/shop',
      name: 'itemOverwiev',
      component: ItemOverview,
    },
    {
      path: '/shop/:pnr',
      name: 'ProductDetail',
      component: () => import('../pages/ProductDetailView.vue')
    }
  ],
})

export default router;
