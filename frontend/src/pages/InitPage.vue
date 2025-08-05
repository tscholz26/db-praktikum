<script setup>
  import { useRouter } from 'vue-router';
  import { getTestMessage, initDB } from './../services/api.js';

  const router = useRouter();

  async function init() {
    try {
      await initDB();
      router.push('/shop');
    } catch (error) {
      alert("Error during database initialization: " + error);
    }
  }

  async function helloWorld() {
    alert(await getTestMessage());
  }

</script>

<template>
  <button @click="init()">Init</button>
  <!--<button @click="helloWorld()">Test Backend Connection</button>-->
</template>

<style scoped>

</style>