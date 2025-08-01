<script setup>
  import { useRouter } from 'vue-router';
  import { getTestMessage } from './../services/api.js';

  const router = useRouter();

  function init() {
    alert("init")
    router.push('/shop');
  }

  async function helloWorld() {
    alert(await getTestMessage());
  }

</script>

<template>
  <button @click="init()">Init</button>
  <button @click="helloWorld()">Test Backend Connection</button>
</template>

<style scoped>

</style>