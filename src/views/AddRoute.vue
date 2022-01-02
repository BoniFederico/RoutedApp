<template>
  <div id="page">
    <RouteForm></RouteForm>
  </div>
</template>
<script>
import RouteForm from "../components/RouteForm.vue";
import { isLoggedFunction } from "../services/connectors/AuthConnector";
import { useRouter } from "vue-router";

export default {
  components: {
    RouteForm,
  },
  setup() {
    let router = useRouter();
    if (!isLoggedFunction()) {
      router.push({
        name: "Home",
      });
    }
    return {};
  },
  computed: {},
};
</script>
<style scoped>
#page {
  padding-top: 20px;
  width: 90%;
  max-width: 900px;
  margin: auto;
}
</style>
