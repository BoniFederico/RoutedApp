<template>
  <div id="page">
    <div class="main-content">
      <RouteFormUpdate
        v-if="this.selRoute"
        :selectedRoute="this.selRoute"
      ></RouteFormUpdate>
    </div>
  </div>
</template>
<script>
import RouteFormUpdate from "../components/RouteFormUpdate.vue";
import { isLoggedFunction } from "../services/connectors/AuthConnector";
import { useRouter } from "vue-router";
export default {
  components: {
    RouteFormUpdate,
  },
  setup() {
    let router = useRouter();
    if (!isLoggedFunction()) {
      router.push({
        name: "Signin",
        params: { message: "Session expired! Please sign-in!" },
      });
    }
    return {};
  },
  props: ["route"],
  data() {
    return {
      selRoute: undefined,
    };
  },
  created: function () {
    this.selRoute = this.route;
  },
};
</script>
<style scoped lang="scss">
#page {
  & .main-content {
    padding-top: 20px;
    width: 90%;
    max-width: 900px;
    margin: auto;
  }
}
</style>
