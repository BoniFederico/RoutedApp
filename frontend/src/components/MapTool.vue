<template>
  <div>
    <div id="map1"></div>
  </div>
</template>
<script>
import L from "leaflet";
import LeafletDraw from "leaflet-draw";
import "leaflet-draw/dist/leaflet.draw.css";
import "leaflet/dist/leaflet.css";

import { isRouteValid } from "../utils/DataValidators.js";
export default {
  name: "MapTool",
  props: {
    modelValue: String,
  },
  emits: ["update:modelValue"],
  data() {
    return {
      map: null,
      drawnItems: null,
    };
  },
  watch: {
    modelValue: function () {
      if (isRouteValid(this.modelValue)) {
        let drawnItemsLayer = this.drawnItems;
        drawnItemsLayer.clearLayers();
        new L.geoJson(JSON.parse(this.modelValue), {
          onEachFeature: function (feature, layer) {
            layer = layer.setStyle({
              color: "#40908b",
              weight: 4,
              opacity: 0.85,
            });
            drawnItemsLayer.addLayer(layer);
          },
        });
      }
    },
  },
  methods: {
    mapSetup: function () {
      let context = this;
      var mapGJ = L.map("map1").setView([42, 12], 5);
      this.map = mapGJ;
      L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
        /* attribution:
          '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors', */
      }).addTo(mapGJ);

      var drawnItems = new L.FeatureGroup();
      this.drawnItems = drawnItems;
      mapGJ.addLayer(drawnItems);
      var drawControl = new L.Control.Draw({
        draw: {
          polyline: {
            shapeOptions: {
              color: "#40908b",
              weight: 4,
              opacity: 0.85,
            },
          },
          polygon: false,
          circle: false,
          rectangle: false,
          marker: false,
          circlemarker: false,
        },
        edit: {
          featureGroup: drawnItems,
          remove: true,
          edit: true,
        },
      });
      mapGJ.addControl(drawControl);
      mapGJ.on(L.Draw.Event.CREATED, function (event) {
        var layer = event.layer;

        var feature = (layer.feature = layer.feature || {});
        feature.type = feature.type || "Feature";

        drawnItems.addLayer(layer);

        var geoJsonObj = drawnItems.toGeoJSON();
        geoJsonObj.features = drawnItems.toGeoJSON().features.map((f) => {
          f.properties = {};
          return f;
        });

        context.$emit(
          "update:modelValue",
          JSON.stringify(geoJsonObj, undefined, 4)
        );
      });
      mapGJ.on(L.Draw.Event.DELETED, function (event) {
        var geoJsonObj = drawnItems.toGeoJSON();
        geoJsonObj.features = drawnItems.toGeoJSON().features.map((f) => {
          f.properties = {};
          return f;
        });

        context.$emit(
          "update:modelValue",
          JSON.stringify(geoJsonObj, undefined, 4)
        );
      });
      mapGJ.on(L.Draw.Event.EDITED, function (event) {
        var geoJsonObj = drawnItems.toGeoJSON();
        geoJsonObj.features = drawnItems.toGeoJSON().features.map((f) => {
          f.properties = {};
          return f;
        });

        context.$emit(
          "update:modelValue",
          JSON.stringify(geoJsonObj, undefined, 4)
        );
      });
    },
  },
  mounted() {
    this.mapSetup();
  },
};
</script>
<style lang="scss" scoped>
#map1 {
  margin-top: 10px;
  border-radius: 3px;
  height: 300px;
}
</style>
