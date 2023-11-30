<template>
  <span :class="['nbr-msg', isHidden ? 'hidden' : '']">{{
    receivedMsgCount
  }}</span>
</template>
<script>
export default {
  data() {
    return {
      receivedMsgCount: 0,
      isHidden: true,
    };
  },
  mounted() {
    this.receivedMsgCount = 0;
    this.isHidden = true;
  },
};
</script>

<style scoped></style>
