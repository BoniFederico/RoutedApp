<template>
  <div class="main-text pt-5 text-center" id="page">
    <div class="mb-5">
      <img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/vscode.png"
        alt="vscode-logo"
        width="100"
        height="100"
      /><img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/maven.png"
        alt="maven-logo"
        width="100"
        height="100"
      /><img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/java.png"
        alt="java-logo"
        width="90"
        height="90"
      /><img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/tomcat.png"
        alt="tomcat-logo"
        width="90"
        height="90"
      /><img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/javascript.png"
        alt="javascript-logo"
        width="90"
        height="90"
      /><img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/scss.png"
        alt="scss-logo"
        width="90"
        height="90"
      /><img
        class="mt-5 mx-3 form-centered"
        src="@/assets/tech/vue.png"
        alt="vue-logo"
        width="90"
        height="90"
      />
    </div>
    <p class="text-center display-4 lead">
      The website was created for the web programming course project. The
      technologies used on the back end are: Java servlets in
      <strong>TomCat webserver</strong> with <strong>Jersey</strong>,
      <strong>Hibernate</strong>, <strong>MySql</strong>. The technologies used
      on the front end are <strong>Javascript</strong>, <strong>Sass</strong>,
      <strong>Vue.js</strong> with <strong>Vue CLI</strong> . The editor that
      has been used is <strong>Visual Studio Code</strong>, both for back end
      (with <strong>Maven</strong>) and front end.
    </p>
  </div>
</template>
<script>
import { useRouter } from "vue-router";
import { isLoggedFunction } from "../services/connectors/AuthConnector";

export default {
  components: {},
  setup() {
    let router = useRouter();
    if (!isLoggedFunction()) {
      router.push({
        name: "Home",
      });
    }
    return {};
  },
};
</script>

<style lang="scss" scoped>
@import "@/scss/button";

.main-text {
  padding: 20%;
}
#page {
  width: 100%;

  & p {
    font-size: 25px;
  }
}
</style>
