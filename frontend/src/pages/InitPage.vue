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
  <div class="page">
    <button @click="init">Init</button>
  </div>
</template>

<style scoped>
.page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

button {
  cursor: pointer;
  padding: 0.45rem 0.9rem;
  font-size: 1rem;
  border-radius: 4px;
  border: 1px solid #888;
  background-color: #f5f5f5;
  transition: background-color 0.2s ease;
}

button:hover:not(:disabled) {
  background-color: #ddd;
}

</style>