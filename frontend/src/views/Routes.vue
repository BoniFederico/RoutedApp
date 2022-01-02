<template>
  <div id="page">
    <div id="main-content">
      <div id="map-container">
        <MapFilter></MapFilter>
        <Map v-if="isUserLogged"> </Map>
        <RouteTable></RouteTable>
      </div>
    </div>
  </div>
</template>
<script>
import Map from "../components/Map.vue";
import MapFilter from "../components/MapFilter.vue";
import RouteTable from "../components/RouteTable.vue";
import { isLoggedFunction } from "../services/connectors/AuthConnector";
import { useRouter } from "vue-router";
export default {
  components: {
    MapFilter,

    RouteTable,
    Map,
  },
  beforeCreate() {},
  data() {
    return {
      isUserLogged: isLoggedFunction(),
    };
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
<style lang="scss" scoped>
#page {
  width: 100%;
  height: 100vh;
  background-image: linear-gradient(to right, #c9d6ff, #e2e2e2);
  & #map-container {
    margin: 10px;
  }
  & #main-content {
    max-width: 1700px;
    margin: auto;
    border-radius: 3px;
    margin-top: 20px;
    background-color: #f6f6f6;
    padding-bottom: 20px;
  }
}
</style>
