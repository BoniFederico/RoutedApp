<template>
  <div id="wrapper">
    <Multiselect
      @select="handleInput"
      class="selectcol"
      searchable
      placeholder="Select the type of the route"
      v-model="selectedValue"
      :options="options"
    />
  </div>
</template>

<script>
import Multiselect from "@vueform/multiselect";
import { getTypeList } from "../services/connectors/ResourceConnector";

export default {
  name: "DropdownMenu",
  components: {
    Multiselect,
  },
  props: ["modelValue"],
  watch: {
    modelValue: function () {
      this.selectedValue = this.modelValue;
    },
  },
  data() {
    return {
      selectedValue: this.modelValue,

      options: [],
    };
  },
  async created() {
    this.options = await getTypeList();
  },
  methods: {
    handleInput(e) {
      this.$emit("update:modelValue", this.selectedValue);
    },
  },
};
</script>

<style lang="scss">
@import "@vueform/multiselect/themes/default";
@import "@/scss/default/theme-color";
#wrapper .selectcol {
  --ms-ring-width: 1px;
  --ms-ring-color: rgb(192, 192, 192) !important;
  --ms-placeholder-color: #9ca3af;
  --ms-max-height: 10rem;

  --ms-tag-bg: $primary !important;

  --ms-group-label-bg-selected: $primary !important;

  --ms-group-label-bg-selected-pointed: $primary !important;

  --ms-group-label-bg-selected-disabled: $primary !important;

  --ms-option-bg-selected: $primary !important;

  --ms-option-bg-selected-pointed: #5bb9b3 !important;
}
</style>
