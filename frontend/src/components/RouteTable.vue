<template>
  <table selectable @select="selected" class="table table-bordered table-hover">
    <thead>
      <tr class="text-center" style="background-color: #40908b90">
        <th class="fw-normal" scope="col">n°</th>
        <th class="fw-normal" scope="col">Date</th>
        <th class="fw-normal" scope="col">Type</th>
        <th class="fw-normal" scope="col">Main Stages</th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="route in routes"
        :key="route"
        class="text-center"
        @click="openModifyPage(route)"
      >
        <th class="fw-normal" scope="row">
          {{ route.id }}
        </th>
        <td>{{ route.date }}</td>
        <td>{{ route.type }}</td>
        <td>
          <span v-for="(stage, i) in route.stages" :key="stage">
            <span class="no-wrap-content">
              {{ stage.name }} (<span class="coordinate-format"
                >{{ stage.lat }}, {{ stage.len }}</span
              >){{ i !== route.stages.length - 1 ? ", " : "" }}</span
            >
            &ensp;
          </span>
        </td>
      </tr>
    </tbody>
  </table>
</template>
<script>
import {
  STORED_GEOJSON_NAME,
  STORAGE_CHANGE_EVENT,
} from "../Constants/Constants.js";
import {
  getFromStore,
  isStored,
} from "../services/connectors/StorageConnector.js";
import { useRouter } from "vue-router";
export default {
  name: "RouteTable",

  data() {
    return {
      router: useRouter(),
      openModifyPage: (route) => {
        this.router.push({
          name: "UpdateRoute",
          params: { route: JSON.stringify(route) },
        });
      },
      routes: isStored(STORED_GEOJSON_NAME)
        ? (this.routes = JSON.parse(getFromStore(STORED_GEOJSON_NAME)))
        : null,
    };
  },
  mounted() {
    //Set the stages from the route (route and stages are used in separate ways due to some bugs of vue-leaflet Vue.js -> https://github.com/vue-leaflet/vue-leaflet)
    let setStagesInGeoJson = function (routes) {
      let features = [];

      for (const route of routes) {
        for (const f of route.route.features) {
          features.push(f);
        }
        for (const s of route.stages) {
          s.latlen = "( ".concat(s.lat, " , ", s.len, " )");
          features.push({
            type: "Feature",
            geometry: {
              type: "Point",
              coordinates: [s.len, s.lat],
            },
            properties: {},
          });
        }
        features.push();
        route.route.features = features;
        features = [];
      }

      return routes;
    };
    //If stored routes change, refresh the table
    window.addEventListener(STORAGE_CHANGE_EVENT, (event) => {
      event.detail.storage === STORED_GEOJSON_NAME
        ? (this.routes = setStagesInGeoJson(
            JSON.parse(getFromStore(STORED_GEOJSON_NAME))
          ))
        : null;
    });
  },
  methods: {},
};
</script>
<style lang="scss" scoped>
@import "@/scss/table";

.no-wrap-content {
  white-space: nowrap;
}
.coordinate-format {
  font-size: 15px;
  font-style: italic;
}
</style>
