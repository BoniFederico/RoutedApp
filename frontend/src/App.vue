<template>
  <router-view v-slot="{ Component }">
    <nav-bar v-if="!isSigninOrSignupComponent(Component)"></nav-bar>
    <transition name="route" mode="out-in">
      <component :is="Component"> </component>
    </transition>
  </router-view>
</template>
<script>
import NavBar from "./components/NavBar.vue";

export default {
  components: { NavBar },
  methods: {
    isSigninOrSignupComponent: (c) => {
      if (c !== undefined && "type" in c && "components" in c.type) {
        return (
          "SigninForm" in c.type.components || "SignupForm" in c.type.components
        );
      } else {
        return false;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.route-enter-from {
  opacity: 0;
  transform: translateX(100px);
}
.route-enter-active {
  transition: all 0.3s ease-out;
}
.route-leave-to {
  opacity: 0;
  transform: translateX(-100px);
}
.route-leave-active {
  transition: all 0.3s ease-in;
}
</style>
