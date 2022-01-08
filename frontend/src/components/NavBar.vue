<template>
  <nav
    class="lp-3 navbar navbar-light bg-light navbar-expand-sm flex-sm-nowrap flex-wrap shadow -sm"
  >
    <div class="container-fluid">
      <button
        class="navbar-toggler flex-grow-sm-1 flex-grow-0 me-2"
        type="button"
        data-toggle="collapse"
        data-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <span class="navbar-brand flex-grow-1 justify-content-start">
        <router-link style="text-decoration: none" to="/"
          ><h1
            class="main-title display-5 pt-3 pb-4 pl-4 fw-normal form-centered"
          >
            Routed
          </h1></router-link
        >
      </span>

      <div
        v-if="isLogged"
        class="navbar-collapse collapse flex-grow-1 justify-content-center"
        id="navbarNav"
      >
        <ul class="navbar-nav mx-auto">
          <li class="nav-item">
            <router-link to="/Routes" class="nav-link" href="#"
              >Routes</router-link
            >
          </li>
          <li class="nav-item">
            <router-link to="/AddRoute" class="nav-link" href="#"
              >Add Route</router-link
            >
          </li>
          <li class="nav-item">
            <router-link to="/About" class="nav-link" href="#"
              >About</router-link
            >
          </li>
        </ul>
      </div>
      <div v-if="!isLogged" class="flex-grow-1"></div>
      <!-- padding -->
      <div
        class="navbar-collapse collapse flex-grow justify-content-end"
        id="navbarNav2"
      >
        <ul class="navbar-nav mx-auto">
          <li v-if="!isLogged" class="nav-item">
            <router-link to="/Sign-in" class="nav-link" href="#"
              >Sign-in</router-link
            >
          </li>
          <li v-if="!isLogged" class="nav-item">
            <router-link to="/Sign-up" class="nav-link" href="#"
              >Sign-up</router-link
            >
          </li>

          <li v-if="isLogged" class="nav-item">
            <a href="./" @click="logOut" class="nav-link">Log-out</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>
import { useRouter } from "vue-router";
import {
  logOutFunction,
  isLoggedFunction,
} from "../services/connectors/AuthConnector.js";
export default {
  name: "NavBar",
  data() {
    return {
      router: useRouter(),
    };
  },
  setup() {},
  computed: {
    isLogged() {
      return isLoggedFunction();
    },
    logOut() {
      return logOutFunction();
    },
  },
};
</script>
<style scoped lang="scss">
@import "@/scss/default/theme-color";

.navbar {
  background-color: aqua;
  opacity: 0.9;
  font-size: 22px;
}
.main-title {
  font-family: "Sofia", sans-serif;
  color: $primary-title;
  padding-left: 5%;
}
</style>
