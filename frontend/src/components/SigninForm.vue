<template>
  <main class="form-signin shadow-lg p-5 mb-5 bg-white">
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
      <h1 class="h3 pb-2 fw-normal form-centered second-text">Sign-in</h1>

      <div class="mb-2 form-floating">
        <input
          v-model="data.identifier"
          type="username"
          class="form-control"
          id="floatingInput"
          placeholder="name@example.com"
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
          autocomplete="on"
        />
        <label for="floatingPassword">Password</label>
      </div>
      <div class="form-centered">
        <p class="error mb-3 mt-4" v-if="errors.length">
          <span v-for="error in errors" :key="error">{{ error }}</span>
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
        Sign in
      </button>
      <p class="trailer form-centered mt-3">
        New to us?
        <router-link class="second-text" to="/Sign-up">Sign up</router-link>
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
import { authenticateFunction } from "../services/connectors/AuthConnector.js";
import { StatusCodes } from "http-status-codes";
import { isMailValid, isUsernameValid } from "../utils/DataValidators.js";
export default {
  name: "SigninForm",
  props: ["message"],
  data() {
    return {
      errors: [this.message],
      loading: false,
      submit: async () => {
        this.errors = [];
        this.loading = true;
        if (
          !isMailValid(this.data.identifier) &&
          !isUsernameValid(this.data.identifier)
        ) {
          this.loading = false;
          this.errors.push("Please provide a correct email or username!");
          return;
        }

        await authenticateFunction(this.data)
          .then((respStatus) => {
            if (respStatus === StatusCodes.UNAUTHORIZED) {
              this.loading = false;
              this.errors.push("Credentials are not valid!");
            } else if (respStatus === StatusCodes.OK) {
              this.loading = false;

              this.router.push("/");
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
      identifier: "",
      password: "",
    });
    let router = useRouter();

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

/* .form-signin .checkbox {
  font-weight: 400;
}
 */
.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="email"] {
  margin-bottom: -1px;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
}
</style>
