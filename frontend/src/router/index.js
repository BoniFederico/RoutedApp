import { createRouter, createWebHistory } from "vue-router";
import Signin from "@/views/Signin.vue";
import Signup from "@/views/Signup.vue";
import Routes from "@/views/Routes.vue";
import AddRoute from "@/views/AddRoute.vue";
import UpdateRoute from "@/views/UpdateRoute.vue";
import About from "@/views/About.vue";
import Home from "@/views/Home.vue";
const routes = [
  {
    path: "/Sign-in",
    name: "Signin",
    component: Signin,
    props: true,
  },
  {
    path: "/Sign-up",
    name: "Signup",
    component: Signup,
  },
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: { transition: "zoom" },
  },
  {
    path: "/Routes",
    name: "Routes",
    component: Routes,
  },
  {
    path: "/AddRoute",
    name: "AddRoute",
    component: AddRoute,
  },
  {
    path: "/UpdateRoute",
    name: "UpdateRoute",
    component: UpdateRoute,
    props: true,
  },
  {
    path: "/About",
    name: "About",
    component: About,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
