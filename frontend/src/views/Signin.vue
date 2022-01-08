<template>
  <div id="page">
    <SigninForm :message="message" class="main-content p-5" />
  </div>
</template>
<script>
import SigninForm from "@/components/SigninForm";

export default {
  components: {
    SigninForm,
  },
  props: ["message"],
};
</script>
<style lang="scss" scoped>
#page {
  height: 100vh;
  background-image: linear-gradient(to right, #c9d6ff, #e2e2e2);
  & .main-content {
    margin: 0;
    position: absolute;
    top: 50%;
    left: 50%;
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
  }
}
</style>
