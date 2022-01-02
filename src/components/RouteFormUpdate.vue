<template>
  <main
    style="border-radius: 5px"
    class="shadow-lg px-5 pb-4 pt-4 mb-2 bg-white"
  >
    <form @submit.prevent="submitUpdate">
      <h1 class="main-title display-3 pt-3 pb-3 fw-normal form-centered">
        Update Route
      </h1>

      <div class="row mb-3">
        <label for="routeJson" class="col-sm-2 col-form-label formLabel"
          >Route
        </label>
        <div class="col-sm-10">
          <textarea
            @change="formatJson"
            v-model="data.route"
            class="form-control"
            id="routeJson"
            placeholder="Past your GeoJson route here..."
          />
        </div>
      </div>
      <div class="row mb-3">
        <label for="datePic" class="col-sm-2 col-form-label formLabel"
          >Date</label
        >
        <div class="col-sm-10">
          <Datepicker
            id="datePic"
            :format="format"
            v-model="data.date"
            placeholder="Select the date of the route"
            :maxDate="new Date()"
            autoApply
            :enableTimePicker="false"
            :partialRange="false"
          >
          </Datepicker>
        </div>
      </div>

      <div class="row mb-3">
        <label for="dropdownMenu" class="col-sm-2 col-form-label formLabel"
          >Type</label
        >
        <div class="col-sm-10">
          <DropdownMenu
            :modelValue="data.type"
            v-model:modelValue="data.type"
          ></DropdownMenu>
        </div>
      </div>
      <h3
        class="col-sm-2 mt-1 col-form-label formLabel form-centered"
        formLabel
      >
        Main Stages
      </h3>
      <hr />
      <div
        v-for="stage in mainStages"
        :key="stage"
        class="row align-items-center row mb-3 g-3"
      >
        <div class="col-3">
          <label class="visually-hidden" for="inputLat">Latitude</label>
          <input
            v-model="stage.lat"
            class="form-control"
            id="inputLat"
            placeholder="Latitude"
          />
        </div>
        <div class="col-3">
          <label class="visually-hidden" for="inputLong">Longitude</label>
          <input
            v-model="stage.len"
            class="form-control"
            id="inputLong"
            placeholder="Longitude"
          />
        </div>
        <div class="col-5">
          <label class="visually-hidden" for="stageName">Name</label>
          <input
            v-model="stage.name"
            class="form-control"
            id="stageName"
            placeholder="Name"
          />
        </div>
        <div class="col-1">
          <button
            style="width: 100%"
            @click="addField(mainStages)"
            type="button"
            class="btn btn-primary shadow-none"
          >
            +
          </button>
        </div>
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
        Update Route {{ data.id === "" ? null : "n°" + data.id }}
      </button>
      <button
        class="mt-2 w-100 btn btn-lg btn-delete shadow-none"
        @click="submitDelete"
        type="button"
      >
        Delete Route {{ data.id === "" ? null : "n°" + data.id }}
      </button>
      <p class="trailer mt-3 text-muted form-centered">
        Routed App ( &copy; 2021 )
      </p>
    </form>
  </main>
</template>
<script>
import Datepicker from "vue3-date-time-picker";
import DropdownMenu from "../components/DropdownMenu.vue";
import { StatusCodes } from "http-status-codes";
import "vue3-date-time-picker/dist/main.css";
import {
  updateRoute,
  deleteRoute,
} from "../services/connectors/ResourceConnector.js";
import { areStagesValid, isRouteValid } from "../utils/DataValidators.js";
import { reactive } from "vue";
export default {
  name: "RouteFormUpdate",
  components: { Datepicker, DropdownMenu },
  props: ["selectedRoute"],
  data() {
    return {
      loading: false,
      infos: [],
      errors: [],
      mainStages: JSON.parse(this.selectedRoute).stages,
      value: null,
      submitDelete: async () => {
        this.errors = [];
        deleteRoute(this.data.id).then((respStatus) => {
          this.loading = false;
          if (respStatus === StatusCodes.UNAUTHORIZED) {
            this.router.push({
              name: "Signin",
              params: { message: "Session expired! Please sign-in!" },
            });
          }
          if (respStatus !== StatusCodes.OK) {
            this.errors.push(
              "An internal error occour! Please wait some minutes or try to log-out!"
            );
            return;
          } else {
            this.mainStages = [{ lat: "", len: "", name: "" }];
            this.data.route = "";
            this.data.date = "";
            this.data.id = "";
            this.infos.push("Route successfully deleted!");
          }
        });
      },
      submitUpdate: async () => {
        this.errors = [];
        this.route = [];
        if (!areStagesValid(this.mainStages)) {
          this.errors.push("Stages contain invalid coordinates or name. ");
        }
        if (!isRouteValid(this.data.route)) {
          this.errors.push("GeoJson seems to be incorrect! ");
        }
        if (this.data.date === "") {
          this.errors.push("Route cannot have no date! ");
        }
        if (this.data.type === "") {
          this.errors.push("Route cannot have no type! ");
        }
        if (this.errors.length !== 0) {
          return;
        }
        let newStages = [];
        this.mainStages.forEach((stage) =>
          newStages.push({
            name: stage.name,
            lat: parseFloat(stage.lat),
            len: parseFloat(stage.len),
          })
        );

        this.loading = true;

        updateRoute(
          this.data.route,
          new Date(
            this.data.date.getFullYear(),
            this.data.date.getMonth(),
            this.data.date.getDate()
          ).toLocaleDateString("it-it", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
          }),
          this.data.type,
          newStages,
          this.data.id
        ).then((respStatus) => {
          this.loading = false;
          if (respStatus === StatusCodes.UNAUTHORIZED) {
            this.router.push({
              name: "Signin",
              params: { message: "Session expired! Please sign-in!" },
            });
          }
          if (respStatus !== StatusCodes.OK) {
            this.errors.push(
              "An internal error occour! Please wait some minutes or try to log-out!"
            );
            return;
          } else {
            this.mainStages = [{ lat: "", len: "", name: "" }];
            this.data.route = "";
            this.data.date = "";
            this.data.id = "";
            this.infos.push("Route successfully updated!");
          }
        });
      },
    };
  },
  methods: {
    formatJson() {
      try {
        let obj = JSON.parse(this.data.route);
        this.data.route = JSON.stringify(obj, undefined, 4);
      } catch (error) {
        console.log("no geojson");
      }
    },
    addField(v) {
      v.push({ lat: "", len: "", name: "" });
    },
    insertRouteData() {
      let routeParsed = JSON.parse(this.selectedRoute);
      this.mainStages = routeParsed.stages;
      if (this.mainStages.length === 0) {
        this.mainStages = [{ lat: "", len: "", name: "" }];
      }
      let date = routeParsed.date.split("/");
      this.data.type = routeParsed.type;
      this.data.route = JSON.stringify(routeParsed.route);
      this.data.date = new Date(date[2], date[1] - 1, date[0]);
      this.data.id = routeParsed.id;
    },
  },
  setup() {
    let data = reactive({
      route: "",
      date: "",
      type: "",
      id: Number,
    });

    const format = (date) => {
      return new Date(
        date.getFullYear(),
        date.getMonth(),
        date.getDate()
      ).toLocaleDateString("en-us", {
        weekday: "long",
        year: "numeric",
        month: "short",
        day: "numeric",
      });
    };
    return {
      format,
      data,
    };
  },
  mounted() {
    this.insertRouteData();
    this.formatJson();
  },
};
</script>
<style lang="scss" scoped>
@import "@/scss/button";
@import "@/scss/route-form";
</style>
