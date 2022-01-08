<template>
  <div id="bar">
    <div id="welcoming-sentence">
      Select a range of <strong>dates</strong> in order to visualize your
      routes! Select them in the table below for <strong>updating</strong> or
      <strong>deleting</strong> operations.
    </div>
    <div class="filter-element">
      <Datepicker
        :format="format"
        v-model="date"
        range
        placeholder="Select the dates of the routes to view"
        textInput
        :maxDate="new Date()"
        autoApply
        :enableTimePicker="false"
        :partialRange="false"
      >
      </Datepicker>
    </div>
  </div>
</template>
<script>
import Datepicker from "vue3-date-time-picker";
import "vue3-date-time-picker/dist/main.css";
import { saveRouteFromRange } from "../services/connectors/ResourceConnector.js";
import { ref } from "vue";
import { StatusCodes } from "http-status-codes";
import { useRouter } from "vue-router";
export default {
  name: "MapFilter",
  components: {
    Datepicker,
  },
  watch: {
    //Change routes stored in localStorage when date change on date picker
    date: function () {
      if (this.date !== null) {
        saveRouteFromRange(this.formatDate(this.date)).then((status) => {
          if (status === StatusCodes.UNAUTHORIZED) {
            this.router.push({
              name: "Signin",
              params: { message: "Session expired! Please sign-in!" },
            });
          }
        });
      }
    },
  },
  setup() {
    const date = ref(new Date());
    const formatDate = (date) => {
      return `${("0" + date[0].getDate()).slice(-2)}-${(
        "0" +
        (date[0].getMonth() + 1)
      ).slice(-2)}-${date[0].getFullYear()}/${("0" + date[1].getDate()).slice(
        -2
      )}-${("0" + (date[1].getMonth() + 1)).slice(
        -2
      )}-${date[1].getFullYear()}`;
    };

    const format = (date) => {
      if (
        date[0].getDate() === date[1].getDate() &&
        date[0].getMonth() === date[1].getMonth() &&
        date[0].getFullYear() === date[1].getFullYear()
      ) {
        return new Date(
          date[0].getFullYear(),
          date[0].getMonth(),
          date[0].getDate()
        ).toLocaleDateString("en-us", {
          weekday: "long",
          year: "numeric",
          month: "short",
          day: "numeric",
        });
      }
      return new Date(
        date[0].getFullYear(),
        date[0].getMonth(),
        date[0].getDate()
      )
        .toLocaleDateString("en-us", {
          weekday: "long",
          year: "numeric",
          month: "short",
          day: "numeric",
        })
        .concat(
          " - ",
          new Date(
            date[1].getFullYear(),
            date[1].getMonth(),
            date[1].getDate()
          ).toLocaleDateString("en-us", {
            weekday: "long",
            year: "numeric",
            month: "short",
            day: "numeric",
          })
        );
    };
    return {
      router: useRouter(),
      formatDate,
      date,
      format,
    };
  },
};
</script>
<style lang="scss" scoped>
@import "@/scss/default/theme-color";
@media screen and (max-width: 900px) {
  #welcomingSentence {
    width: 100% !important;
  }
  .filter-element {
    width: 100% !important;
  }
}
#bar {
  padding-top: 10px;
  padding-bottom: 10px;
}
strong {
  font-weight: 500;
}
#welcoming-sentence {
  font-size: 17px;
  float: left;
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 8px;
  padding-bottom: 10px;
  width: 60%;
}
.filter-element {
  float: right;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
  width: 40%;
}
.dp__theme_light {
  --dp-background-color: #ffffff;
  --dp-text-color: #212121;
  --dp-hover-color: #f3f3f3;
  --dp-hover-text-color: #212121;
  --dp-hover-icon-color: #959595;
  --dp-primary-color: $info !important;
  --dp-primary-text-color: #f8f5f5;
  --dp-secondary-color: #c0c4cc;
  --dp-border-color: #ddd;
  --dp-border-color-hover: #aaaeb7;
  --dp-disabled-color: #f6f6f6;
  --dp-scroll-bar-background: #f3f3f3;
  --dp-scroll-bar-color: #959595;
  --dp-success-color: #478346;
  --dp-success-color-disabled: #a3d9b1;
  --dp-icon-color: #959595;
  --dp-danger-color: #ff6f60;
}
</style>
