<template>
  <main class="form-signup shadow-lg p-5 mb-5 bg-white">
    <form @submit.prevent="submit">
      <img
        class="mb-4 form-centered"
        src="@/assets/logo.png"
        alt=""
        width="100"
        height="100"
      />
      <h1 class="main-title display-3 pt-3 pb-4 fw-normal form-centered">
        Routed
      </h1>
      <h1 class="h3 pb-2 fw-normal form-centered second-text">Sign-up</h1>

      <div class="mb-2 form-floating">
        <input
          v-model="data.email"
          type="mail"
          class="form-control"
          id="floatingMail"
          placeholder="example@email.com"
        />
        <label for="floatingMail">Email</label>
      </div>
      <div class="mb-2 form-floating">
        <input
          v-model="data.username"
          type="username"
          class="form-control"
          id="floatingInput"
          placeholder="username"
        />
        <label for="floatingInput">Username</label>
      </div>
      <div class="mb-2 form-floating">
        <input
          v-model="data.password"
          type="password"
          class="form-control"
          id="floatingPassword"
          placeholder="Passsword"
          s
        />
        <label for="floatingPassword">Password</label>
      </div>
      <div class="form-centered">
        <p class="error mb-3 mt-4" v-if="errors.length">
          <span v-for="error in errors" :key="error">{{ error }}</span>
        </p>
        <p class="info mb-3 mt-4" v-if="infos.length">
          <span v-for="info in infos" :key="info">{{ info }}</span>
        </p>
      </div>
      <div
        v-show="loading"
        class="form-centered mt-2 mb-2 spinner-border loading"
        role="status"
      >
        <span class="sr-only"></span>
      </div>
      <button
        class="mt-2 w-100 btn btn-lg btn-primary shadow-none"
        type="submit"
      >
        Sign up
      </button>
      <p class="trailer form-centered mt-3">
        <router-link class="second-text" to="/Sign-in">Sign in</router-link>
      </p>
      <p class="trailer mt-3 mb-3 text-muted form-centered">
        Routed App ( &copy; 2021 )
      </p>
    </form>
  </main>
</template>

<script>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { registerFunction } from "../services/connectors/AuthConnector.js";
import { StatusCodes } from "http-status-codes";
import { isMailValid, isUsernameValid } from "../utils/DataValidators.js";

export default {
  name: "SignupForm",
  data() {
    return {
      errors: [],
      infos: [],
      loading: false,
      submit: async () => {
        this.loading = true;
        this.errors = [];
        this.infos = [];

        if (!isMailValid(this.data.email)) {
          this.loading = false;
          this.errors.push("Please provide a correct email!");
          return;
        }
        if (this.data.username.length < 6 || this.data.password.length < 6) {
          this.loading = false;
          this.errors.push(
            "Username and password must have a minimum of 6 characters! "
          );
          return;
        }
        if (!isUsernameValid(this.data.username)) {
          this.loading = false;
          this.errors.push("Username must have only letters and numbers!");
          return;
        }
        await registerFunction(this.data)
          .then((respStatus) => {
            if (respStatus === StatusCodes.CONFLICT) {
              this.loading = false;
              this.errors.push(
                "User with this username or email already exist!"
              );
            } else if (respStatus === StatusCodes.OK) {
              this.loading = false;
              this.infos.push(
                "Now you have an account! Soon you will be redirected to the sign-in page!"
              );
              setTimeout(() => {
                this.router.push("/Sign-in");
              }, 5000);
            } else {
              this.loading = false;

              this.errors.push(
                "An internal error occour! Please wait some minutes or try to refresh the page!"
              );
            }
          })
          .catch();
      },
    };
  },
  setup() {
    let data = reactive({
      username: "",
      email: "",
      password: "",
    });
    const router = useRouter();

    return {
      router,
      data,
    };
  },
};
</script>

<style scoped lang="scss">
@import "@/scss/button";
@import "@/scss/auth-form";

.form-signup input[type="email"] {
  margin-bottom: -1px;
}

.form-signup input[type="password"] {
  margin-bottom: 10px;
}
</style>
