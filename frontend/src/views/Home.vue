<template>
  <div class="pt-5 text-center" id="page">
    <div v-if="!isLogged">
      <h1 class="main-title display-1 pt-3 pb-3 fw-normal text-center">
        Set your routes, now.
      </h1>
      <p class="m-3 text-center display-4 lead">
        Start saving your daily trips with a few clicks, and access them
        whenever you want.
      </p>
      <button
        type="button"
        @click="loginRedirect"
        class="mt-5 mb-5 btn btn-primary shadow-none form-center"
      >
        Get Started
      </button>
    </div>

    <div v-if="isLogged">
      <h1 class="main-title display-1 pt-2 mt-5 pb-3 fw-normal text-center">
        Hi {{ username }}!
      </h1>
      <p class="m-3 text-center display-4 lead">
        Follow the links on the navigation bar to add or view your routes.
      </p>
    </div>
    <img
      class="mt-5 form-centered"
      src="@/assets/logo.png"
      alt=""
      width="128"
      height="128"
    />
  </div>
</template>
<script>
import { useRouter } from "vue-router";
import { isLoggedFunction } from "../services/connectors/AuthConnector";
import { getUserInfo } from "../services/connectors/ResourceConnector";
export default {
  components: {},
  data() {
    return {
      username: null,
      router: useRouter(),
      loginRedirect: () => {
        this.router.push("/Sign-up");
      },
    };
  },
  async created() {
    if (isLoggedFunction()) {
      const un = await getUserInfo();
      this.username = un.username;
    }
  },
  computed: {
    isLogged() {
      return isLoggedFunction();
    },
  },
};
</script>

<style lang="scss">
@import "@/scss/button";

.main-title {
  font-family: "Sofia", sans-serif;
  text-decoration: none;
  color: #264947;
}

#page {
  width: 100%;

  /*   background-image: url("https://i.ibb.co/TLRYrzZ/pngwing-com-4.png");
  background-size: cover; */
  & p {
    font-size: 25px;
  }
  & button {
    width: 30%;
    height: 10%;
    font-size: 20px;
  }
}
</style>
