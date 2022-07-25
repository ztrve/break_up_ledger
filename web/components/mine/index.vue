<template>
  <view class="mine">
    mine
  </view>
</template>

<script>
import { reactive, toRefs } from 'vue';
export default {
  name: 'Mine',
  components: {

  },
  setup() {
    const state = reactive({
    });

    const methods = {

    };

    return {
      ...methods,
      ...toRefs(state)
    }
  }
}
</script>

<style lang="scss">

</style>
