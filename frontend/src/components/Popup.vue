<template>
  <div id="popup-title">
    {{ stage.name + "  " + "(" + stage.lat + " , " + stage.len + ")" }}
    <hr />
  </div>

  <div class="row">
    <div class="column-1">
      <img
        id="info-image"
        src="https://img.icons8.com/cotton/50/000000/info.png"
      />
    </div>

    <div class="column-2">
      This is your stage <b>{{ stage.name }}</b
      >, visited in <b> {{ route.date }}</b
      >. Your route's type was: <b> {{ route.type }}</b
      >.
    </div>
  </div>
</template>
<script>
export default {
  name: "Popup",

  props: ["route", "stage"],
};
</script>
<style lang="scss" scoped>
#popup-title {
  font-size: 16px;
  line-height: 24px;
}

#info-image {
  margin-top: -6px;
  margin-right: 2px;
  height: 40px;
  width: 40px;
}
.row {
  & .column-1 {
    float: left;
    width: 20%;
  }
  & .column-2 {
    float: left;
    width: 80%;
  }
}
</style>
