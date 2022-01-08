<template>
  <div>
    <div id="map1"></div>
    <div id="marker-error"></div>
  </div>
</template>
<script>
import L from "leaflet";
import LeafletDraw from "leaflet-draw";
import "leaflet-draw/dist/leaflet.draw.css";
import "leaflet/dist/leaflet.css";
import { icon, Marker } from "leaflet";
import("leaflet-geometryutil");
import { isRouteValid } from "../utils/DataValidators.js";
export default {
  name: "MapTool",
  props: {
    modelValue: String,
    startEditOnly: Boolean,
  },
  emits: ["update:modelValue"],
  data() {
    return {
      drawControl: undefined,
      drawControlEditOnly: undefined,
      markerError: true,
      markerDistanceError: 0,
      map: null,
      drawnItems: null,
    };
  },
  watch: {
    //It will be executed when the geojson passed changes
    modelValue: function () {
      //If geoJson is null delete all drawn layers
      if (this.modelValue === "") {
        let drawnItemsLayer = this.drawnItems;
        drawnItemsLayer.clearLayers();
      }
      //If geoJson is valid clear layers and add new ones
      if (isRouteValid(this.modelValue)) {
        let drawnItemsLayer = this.drawnItems;
        drawnItemsLayer.clearLayers();

        new L.geoJson(JSON.parse(this.modelValue), {
          onEachFeature: function (feature, layer) {
            if (feature.geometry.type === "LineString") {
              layer = layer.setStyle({
                color: "#40908b",
                weight: 4,
                opacity: 0.85,
              });
            }
            drawnItemsLayer.addLayer(layer);
          },
        });
        this.map.invalidateSize(); //refresh map
      }
    },
  },
  methods: {
    mapSetup: function () {
      //We have to redefine marker icon due to a leaflet draw bug-> https://github.com/Leaflet/Leaflet/issues/4968
      const iconUrl =
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAApCAYAAADAk4LOAAAFgUlEQVR4Aa1XA5BjWRTN2oW17d3YaZtr2962HUzbDNpjszW24mRt28p47v7zq/bXZtrp/lWnXr337j3nPCe85NcypgSFdugCpW5YoDAMRaIMqRi6aKq5E3YqDQO3qAwjVWrD8Ncq/RBpykd8oZUb/kaJutow8r1aP9II0WmLKLIsJyv1w/kqw9Ch2MYdB++12Onxee/QMwvf4/Dk/Lfp/i4nxTXtOoQ4pW5Aj7wpici1A9erdAN2OH64x8OSP9j3Ft3b7aWkTg/Fm91siTra0f9on5sQr9INejH6CUUUpavjFNq1B+Oadhxmnfa8RfEmN8VNAsQhPqF55xHkMzz3jSmChWU6f7/XZKNH+9+hBLOHYozuKQPxyMPUKkrX/K0uWnfFaJGS1QPRtZsOPtr3NsW0uyh6NNCOkU3Yz+bXbT3I8G3xE5EXLXtCXbbqwCO9zPQYPRTZ5vIDXD7U+w7rFDEoUUf7ibHIR4y6bLVPXrz8JVZEql13trxwue/uDivd3fkWRbS6/IA2bID4uk0UpF1N8qLlbBlXs4Ee7HLTfV1j54APvODnSfOWBqtKVvjgLKzF5YdEk5ewRkGlK0i33Eofffc7HT56jD7/6U+qH3Cx7SBLNntH5YIPvODnyfIXZYRVDPqgHtLs5ABHD3YzLuespb7t79FY34DjMwrVrcTuwlT55YMPvOBnRrJ4VXTdNnYug5ucHLBjEpt30701A3Ts+HEa73u6dT3FNWwflY86eMHPk+Yu+i6pzUpRrW7SNDg5JHR4KapmM5Wv2E8Tfcb1HoqqHMHU+uWDD7zg54mz5/2BSnizi9T1Dg4QQXLToGNCkb6tb1NU+QAlGr1++eADrzhn/u8Q2YZhQVlZ5+CAOtqfbhmaUCS1ezNFVm2imDbPmPng5wmz+gwh+oHDce0eUtQ6OGDIyR0uUhUsoO3vfDmmgOezH0mZN59x7MBi++WDL1g/eEiU3avlidO671bkLfwbw5XV2P8Pzo0ydy4t2/0eu33xYSOMOD8hTf4CrBtGMSoXfPLchX+J0ruSePw3LZeK0juPJbYzrhkH0io7B3k164hiGvawhOKMLkrQLyVpZg8rHFW7E2uHOL888IBPlNZ1FPzstSJM694fWr6RwpvcJK60+0HCILTBzZLFNdtAzJaohze60T8qBzyh5ZuOg5e7uwQppofEmf2++DYvmySqGBuKaicF1blQjhuHdvCIMvp8whTTfZzI7RldpwtSzL+F1+wkdZ2TBOW2gIF88PBTzD/gpeREAMEbxnJcaJHNHrpzji0gQCS6hdkEeYt9DF/2qPcEC8RM28Hwmr3sdNyht00byAut2k3gufWNtgtOEOFGUwcXWNDbdNbpgBGxEvKkOQsxivJx33iow0Vw5S6SVTrpVq11ysA2Rp7gTfPfktc6zhtXBBC+adRLshf6sG2RfHPZ5EAc4sVZ83yCN00Fk/4kggu40ZTvIEm5g24qtU4KjBrx/BTTH8ifVASAG7gKrnWxJDcU7x8X6Ecczhm3o6YicvsLXWfh3Ch1W0k8x0nXF+0fFxgt4phz8QvypiwCCFKMqXCnqXExjq10beH+UUA7+nG6mdG/Pu0f3LgFcGrl2s0kNNjpmoJ9o4B29CMO8dMT4Q5ox8uitF6fqsrJOr8qnwNbRzv6hSnG5wP+64C7h9lp30hKNtKdWjtdkbuPA19nJ7Tz3zR/ibgARbhb4AlhavcBebmTHcFl2fvYEnW0ox9xMxKBS8btJ+KiEbq9zA4RthQXDhPa0T9TEe69gWupwc6uBUphquXgf+/FrIjweHQS4/pduMe5ERUMHUd9xv8ZR98CxkS4F2n3EUrUZ10EYNw7BWm9x1GiPssi3GgiGRDKWRYZfXlON+dfNbM+GgIwYdwAAAAASUVORK5CYII=";
      const shadowUrl =
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACkAAAApCAQAAAACach9AAACMUlEQVR4Ae3ShY7jQBAE0Aoz/f9/HTMzhg1zrdKUrJbdx+Kd2nD8VNudfsL/Th///dyQN2TH6f3y/BGpC379rV+S+qqetBOxImNQXL8JCAr2V4iMQXHGNJxeCfZXhSRBcQMfvkOWUdtfzlLgAENmZDcmo2TVmt8OSM2eXxBp3DjHSMFutqS7SbmemzBiR+xpKCNUIRkdkkYxhAkyGoBvyQFEJEefwSmmvBfJuJ6aKqKWnAkvGZOaZXTUgFqYULWNSHUckZuR1HIIimUExutRxwzOLROIG4vKmCKQt364mIlhSyzAf1m9lHZHJZrlAOMMztRRiKimp/rpdJDc9Awry5xTZCte7FHtuS8wJgeYGrex28xNTd086Dik7vUMscQOa8y4DoGtCCSkAKlNwpgNtphjrC6MIHUkR6YWxxs6Sc5xqn222mmCRFzIt8lEdKx+ikCtg91qS2WpwVfBelJCiQJwvzixfI9cxZQWgiSJelKnwBElKYtDOb2MFbhmUigbReQBV0Cg4+qMXSxXSyGUn4UbF8l+7qdSGnTC0XLCmahIgUHLhLOhpVCtw4CzYXvLQWQbJNmxoCsOKAxSgBJno75avolkRw8iIAFcsdc02e9iyCd8tHwmeSSoKTowIgvscSGZUOA7PuCN5b2BX9mQM7S0wYhMNU74zgsPBj3HU7wguAfnxxjFQGBE6pwN+GjME9zHY7zGp8wVxMShYX9NXvEWD3HbwJf4giO4CFIQxXScH1/TM+04kkBiAAAAAElFTkSuQmCC";
      const iconDefault = icon({
        iconUrl,
        shadowUrl,
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        tooltipAnchor: [16, -28],
        shadowSize: [41, 41],
      });
      Marker.prototype.options.icon = iconDefault;
      const defaultMarker =
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAApCAYAAADAk4LOAAAFgUlEQVR4Aa1XA5BjWRTN2oW17d3YaZtr2962HUzbDNpjszW24mRt28p47v7zq/bXZtrp/lWnXr337j3nPCe85NcypgSFdugCpW5YoDAMRaIMqRi6aKq5E3YqDQO3qAwjVWrD8Ncq/RBpykd8oZUb/kaJutow8r1aP9II0WmLKLIsJyv1w/kqw9Ch2MYdB++12Onxee/QMwvf4/Dk/Lfp/i4nxTXtOoQ4pW5Aj7wpici1A9erdAN2OH64x8OSP9j3Ft3b7aWkTg/Fm91siTra0f9on5sQr9INejH6CUUUpavjFNq1B+Oadhxmnfa8RfEmN8VNAsQhPqF55xHkMzz3jSmChWU6f7/XZKNH+9+hBLOHYozuKQPxyMPUKkrX/K0uWnfFaJGS1QPRtZsOPtr3NsW0uyh6NNCOkU3Yz+bXbT3I8G3xE5EXLXtCXbbqwCO9zPQYPRTZ5vIDXD7U+w7rFDEoUUf7ibHIR4y6bLVPXrz8JVZEql13trxwue/uDivd3fkWRbS6/IA2bID4uk0UpF1N8qLlbBlXs4Ee7HLTfV1j54APvODnSfOWBqtKVvjgLKzF5YdEk5ewRkGlK0i33Eofffc7HT56jD7/6U+qH3Cx7SBLNntH5YIPvODnyfIXZYRVDPqgHtLs5ABHD3YzLuespb7t79FY34DjMwrVrcTuwlT55YMPvOBnRrJ4VXTdNnYug5ucHLBjEpt30701A3Ts+HEa73u6dT3FNWwflY86eMHPk+Yu+i6pzUpRrW7SNDg5JHR4KapmM5Wv2E8Tfcb1HoqqHMHU+uWDD7zg54mz5/2BSnizi9T1Dg4QQXLToGNCkb6tb1NU+QAlGr1++eADrzhn/u8Q2YZhQVlZ5+CAOtqfbhmaUCS1ezNFVm2imDbPmPng5wmz+gwh+oHDce0eUtQ6OGDIyR0uUhUsoO3vfDmmgOezH0mZN59x7MBi++WDL1g/eEiU3avlidO671bkLfwbw5XV2P8Pzo0ydy4t2/0eu33xYSOMOD8hTf4CrBtGMSoXfPLchX+J0ruSePw3LZeK0juPJbYzrhkH0io7B3k164hiGvawhOKMLkrQLyVpZg8rHFW7E2uHOL888IBPlNZ1FPzstSJM694fWr6RwpvcJK60+0HCILTBzZLFNdtAzJaohze60T8qBzyh5ZuOg5e7uwQppofEmf2++DYvmySqGBuKaicF1blQjhuHdvCIMvp8whTTfZzI7RldpwtSzL+F1+wkdZ2TBOW2gIF88PBTzD/gpeREAMEbxnJcaJHNHrpzji0gQCS6hdkEeYt9DF/2qPcEC8RM28Hwmr3sdNyht00byAut2k3gufWNtgtOEOFGUwcXWNDbdNbpgBGxEvKkOQsxivJx33iow0Vw5S6SVTrpVq11ysA2Rp7gTfPfktc6zhtXBBC+adRLshf6sG2RfHPZ5EAc4sVZ83yCN00Fk/4kggu40ZTvIEm5g24qtU4KjBrx/BTTH8ifVASAG7gKrnWxJDcU7x8X6Ecczhm3o6YicvsLXWfh3Ch1W0k8x0nXF+0fFxgt4phz8QvypiwCCFKMqXCnqXExjq10beH+UUA7+nG6mdG/Pu0f3LgFcGrl2s0kNNjpmoJ9o4B29CMO8dMT4Q5ox8uitF6fqsrJOr8qnwNbRzv6hSnG5wP+64C7h9lp30hKNtKdWjtdkbuPA19nJ7Tz3zR/ibgARbhb4AlhavcBebmTHcFl2fvYEnW0ox9xMxKBS8btJ+KiEbq9zA4RthQXDhPa0T9TEe69gWupwc6uBUphquXgf+/FrIjweHQS4/pduMe5ERUMHUd9xv8ZR98CxkS4F2n3EUrUZ10EYNw7BWm9x1GiPssi3GgiGRDKWRYZfXlON+dfNbM+GgIwYdwAAAAASUVORK5CYII=";

      const defaultMarkerShadow =
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACkAAAApCAQAAAACach9AAACMUlEQVR4Ae3ShY7jQBAE0Aoz/f9/HTMzhg1zrdKUrJbdx+Kd2nD8VNudfsL/Th///dyQN2TH6f3y/BGpC379rV+S+qqetBOxImNQXL8JCAr2V4iMQXHGNJxeCfZXhSRBcQMfvkOWUdtfzlLgAENmZDcmo2TVmt8OSM2eXxBp3DjHSMFutqS7SbmemzBiR+xpKCNUIRkdkkYxhAkyGoBvyQFEJEefwSmmvBfJuJ6aKqKWnAkvGZOaZXTUgFqYULWNSHUckZuR1HIIimUExutRxwzOLROIG4vKmCKQt364mIlhSyzAf1m9lHZHJZrlAOMMztRRiKimp/rpdJDc9Awry5xTZCte7FHtuS8wJgeYGrex28xNTd086Dik7vUMscQOa8y4DoGtCCSkAKlNwpgNtphjrC6MIHUkR6YWxxs6Sc5xqn222mmCRFzIt8lEdKx+ikCtg91qS2WpwVfBelJCiQJwvzixfI9cxZQWgiSJelKnwBElKYtDOb2MFbhmUigbReQBV0Cg4+qMXSxXSyGUn4UbF8l+7qdSGnTC0XLCmahIgUHLhLOhpVCtw4CzYXvLQWQbJNmxoCsOKAxSgBJno75avolkRw8iIAFcsdc02e9iyCd8tHwmeSSoKTowIgvscSGZUOA7PuCN5b2BX9mQM7S0wYhMNU74zgsPBj3HU7wguAfnxxjFQGBE6pwN+GjME9zHY7zGp8wVxMShYX9NXvEWD3HbwJf4giO4CFIQxXScH1/TM+04kkBiAAAAAElFTkSuQmCC";

      var defaultIcon = new L.Icon({
        iconUrl: defaultMarker,
        iconAnchor: [12, 41],
        shadowUrl: defaultMarkerShadow,
      });
      //-------------------------------------------------

      let context = this; // used for dispatch events when route or marker changes
      var mapGJ = L.map("map1", {
        zoomControl: true,
        zoom: 1,
        zoomAnimation: false,
        fadeAnimation: true,
        markerZoomAnimation: true,
      }).setView([42, 12], 5);

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
          marker: { icon: defaultIcon },
          circlemarker: false,
        },
        edit: {
          featureGroup: drawnItems,
          remove: true,
          edit: true,
        },
      });
      var drawControlEditOnly = new L.Control.Draw({
        draw: {
          polyline: false,
          polygon: false,
          circle: false,
          rectangle: false,
          marker: { icon: defaultIcon },
          circlemarker: false,
        },
        edit: {
          featureGroup: drawnItems,
          remove: true,
          edit: true,
        },
      });
      this.drawControl = drawControl;
      this.drawControlEditOnly = drawControlEditOnly;

      //If component is called with startEditOnly prop set to true, the drawControl will start in edit only mode
      if (this.startEditOnly) {
        mapGJ.addControl(drawControlEditOnly);
      } else {
        mapGJ.addControl(drawControl);
      }

      //Display a callout when edit mode start
      mapGJ.on("draw:editstart", function (event) {
        L.DomUtil.get("marker-error").style.display = "inherit";
        L.DomUtil.get("marker-error").innerHTML =
          "   &#9888; Pay attention, if you change the route in edit mode all the markers will be lost!";
        return;
      });
      //Remove callout when edit mode end
      mapGJ.on("draw:editstop", function (event) {
        L.DomUtil.get("marker-error").style.display = "none";
        return;
      });
      //When a layer is created in the map
      mapGJ.on(L.Draw.Event.CREATED, function (event) {
        L.DomUtil.get("marker-error").style.display = "none"; //remove any callout present

        var layer = event.layer;
        var featureType = event.layerType;
        var feature = (layer.feature = layer.feature || {});
        feature.type = feature.type || "Feature";

        //Set drawControl in editmode if the created layer is a polyline (in order to allow only one route)
        if (featureType === "polyline") {
          mapGJ.removeControl(drawControl);
          mapGJ.addControl(drawControlEditOnly);
        }
        //If the created layer is a marker compute the distance between the closest layer. If marker is
        //too far from the closest layer, show a callout and do not allow the creation of marker
        if (featureType === "marker") {
          let closestLayer = L.GeometryUtil.closestLayerSnap(
            mapGJ,
            drawnItems.getLayers(),
            layer._latlng,
            900000,
            false
          );
          let markerDistance = parseInt(
            L.latLng(closestLayer.latlng).distanceTo(layer._latlng)
          );
          if (markerDistance > 200) {
            L.DomUtil.get("marker-error").style.display = "inherit";
            L.DomUtil.get("marker-error").innerHTML =
              ' Marker is <span style="color:red">' +
              parseFloat(markerDistance) / 1000 +
              " km</span>  from the route, the maximum allowed distance is 200 m.";
            return;
          }
        }

        drawnItems.addLayer(layer);

        var geoJsonObj = drawnItems.toGeoJSON();
        geoJsonObj.features = drawnItems.toGeoJSON().features.map((f) => {
          f.properties = {};
          return f;
        });
        //Event for two way binding props ( From Vue3 doc: https://v3.vuejs.org/guide/migration/v-model.html)
        context.$emit(
          "update:modelValue",
          JSON.stringify(geoJsonObj, undefined, 4)
        );
        //Dispatch event for other components: route and markers changed
        window.dispatchEvent(
          new CustomEvent("route-change", {
            detail: {},
          })
        );
      });
      //When a set of layer are deleted
      mapGJ.on(L.Draw.Event.DELETED, function (event) {
        var layers = event.layers.getLayers();
        for (const layer of layers) {
          // If one layer is a line string, add the possibility of adding another line string
          if (layer.feature.geometry.type === "LineString") {
            mapGJ.addControl(drawControl);
            mapGJ.removeControl(drawControlEditOnly);
            drawnItems.clearLayers();
          }
        }
        L.DomUtil.get("marker-error").style.display = "none"; //remove any callout present

        var geoJsonObj = drawnItems.toGeoJSON();
        geoJsonObj.features = drawnItems.toGeoJSON().features.map((f) => {
          f.properties = {};
          return f;
        });
        //Event for two way binding props ( From Vue3 doc: https://v3.vuejs.org/guide/migration/v-model.html)
        context.$emit(
          "update:modelValue",
          JSON.stringify(geoJsonObj, undefined, 4)
        );
        //Dispatch event for other components: route and markers changed
        window.dispatchEvent(
          new CustomEvent("route-change", {
            detail: {},
          })
        );
      });
      //When a set of layers are edited
      mapGJ.on(L.Draw.Event.EDITED, function (event) {
        L.DomUtil.get("marker-error").style.display = "none"; //remove any callout present
        var layers = event.layers.getLayers();

        for (const layer of layers) {
          //If one layer is a line string, set drawControl to line string edit only mode and delete all markers present
          if (layer.feature.geometry.type === "LineString") {
            drawnItems.eachLayer((l) => {
              try {
                drawnItems.removeLayer(l);
              } catch (e) {
                false;
              }
            });
            /*   drawnItems.clearLayers(); */

            drawnItems.addLayer(layer);
            return;
          }
          // If one layer is a marker, check if is near enough to the route
          if (layer.feature.geometry.type === "Point") {
            let closestLayer = L.GeometryUtil.closestLayerSnap(
              mapGJ,
              drawnItems.getLayers().filter((l) => l != layer),
              layer._latlng,
              900000,
              false
            );
            let markerDistance = parseInt(
              L.latLng(closestLayer.latlng).distanceTo(layer._latlng)
            );

            if (markerDistance > 200) {
              L.DomUtil.get("marker-error").style.display = "inherit";
              L.DomUtil.get("marker-error").innerHTML =
                ' Marker has been deleted! It was <span style="color:red">' +
                parseFloat(markerDistance) / 1000 +
                " km</span>  from the route, the maximum allowed distance is 200 m.";

              drawnItems.removeLayer(layer);
              return;
            }
          }
        }

        var geoJsonObj = drawnItems.toGeoJSON();
        geoJsonObj.features = drawnItems.toGeoJSON().features.map((f) => {
          f.properties = {};
          return f;
        });
        //Event for two way binding props ( From Vue3 doc: https://v3.vuejs.org/guide/migration/v-model.html)
        context.$emit(
          "update:modelValue",
          JSON.stringify(geoJsonObj, undefined, 4)
        );
        //Dispatch event for other components: route and markers changed
        window.dispatchEvent(
          new CustomEvent("route-change", {
            detail: {},
          })
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
#marker-error {
  display: none;
  background-color: #ebece9;
  text-align: center;
  margin-top: 2px;
  margin-bottom: 2px;
  padding-bottom: 4px;
  padding-top: 4px;
  font-size: 12px;
  color: black;
}
</style>
