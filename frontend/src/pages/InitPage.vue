<script setup>
  import { useRouter } from 'vue-router';
  const router = useRouter();

  function init() {
    alert("init")
    router.push('/shop');
  }
</script>

<template>
  <button @click="init()">Init</button>
</template>

<style scoped>

</style>