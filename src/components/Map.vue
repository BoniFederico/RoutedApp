<template>
  <link
    rel="stylesheet"
    href="http://code.ionicframework.com/ionicons/1.5.2/css/ionicons.min.css"
  />
  <div id="map-container shadow -sm">
    <l-map id="map" :zoom="zoom" :maxZoom="19" :center="center">
      <l-tile-layer :url="url" :attribution="attribution" />
      <div :key="zoom">
        <l-geo-json
          v-for="el in routes"
          :key="el.route"
          :geojson="el.route"
          :options="options"
          :options-style="styleFunction"
        />
      </div>
      <div v-for="el in routes" :key="el.route">
        <l-marker
          :options="options"
          v-for="stage in el.stages"
          :key="stage"
          :lat-lng="[stage.lat, stage.len]"
        >
          <l-popup>
            <Popup :route="el" :stage="stage"></Popup>
          </l-popup>
        </l-marker>
      </div>
    </l-map>
  </div>
</template>

<script>
import "leaflet/dist/leaflet.css";
import Popup from "../components/Popup.vue";
import {
  STORED_GEOJSON_NAME,
  STORAGE_CHANGE_EVENT,
} from "../Constants/Constants.js";
import {
  getFromStore,
  isStored,
} from "../services/connectors/StorageConnector.js";
import {
  LMap,
  LGeoJson,
  LTileLayer,
  LMarker,
  LPopup,
} from "@vue-leaflet/vue-leaflet";
import { useRouter } from "vue-router";

export default {
  name: "Map",
  components: {
    Popup,
    LPopup,
    LMarker,
    LMap,
    LTileLayer,

    LGeoJson,
  },

  data() {
    return {
      router: useRouter(),
      loading: false,
      show: true,
      showRoutes: false,
      enableTooltip: true,
      map: null,
      zoom: 6,
      center: [42, 12],
      routes: isStored(STORED_GEOJSON_NAME)
        ? JSON.parse(getFromStore(STORED_GEOJSON_NAME))
        : null,
      fillColor: "#e4ce7f",
      url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",

      attribution:
        '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
    };
  },
  mounted() {
    window.addEventListener(STORAGE_CHANGE_EVENT, (event) => {
      event.detail.storage === STORED_GEOJSON_NAME
        ? (this.routes = JSON.parse(getFromStore(STORED_GEOJSON_NAME)))
        : null;
    });
  },
  async beforeMount() {},
  computed: {
    options() {
      return {
        onEachFeature: this.onEachFeatureFunction,
        style: this.styleFeatureFunction,
      };
    },

    styleFunction() {
      const fillColor = this.fillColor;
      return () => {
        return {
          weight: 2,
          color: "#ECEFF1",
          opacity: 1,
          fillColor: fillColor,
          fillOpacity: 1,
        };
      };
    },
    styleFeatureFunction() {
      return function (feature) {
        if ("type" in feature && feature.type === "Feature") {
          return { color: "#40908b", weight: 4, opacity: 0.85 };
        }
      };
    },
    onEachFeatureFunction() {
      return (feature, layer) => {
        layer.bindPopup(
          '<div style="   font-size:16px;line-height:24px; margin-right:5px"> Route n° : ' +
            ("routeid" in feature.properties
              ? parseInt(feature.properties.routeid).toString()
              : "Unknown") +
            "</div> ",
          { permanent: false, sticky: true }
        );
      };
    },
  },
};
</script>
<style scoped>
@import "~leaflet/dist/leaflet.css";
#map {
  height: 420px !important;
  width: 100% !important;
}
.custom-popup .leaflet-popup-content-wrapper {
  background: #2c3e50;
  color: #fff;
  font-size: 16px;
  line-height: 24px;
}
.custom-popup .leaflet-popup-content-wrapper a {
  color: rgba(255, 255, 255, 0.5);
}
.custom-popup .leaflet-popup-tip-container {
  width: 30px;
  height: 15px;
}
.custom-popup .leaflet-popup-tip {
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  border-top: 15px solid #2c3e50;
}
</style>
