<script setup>

</script>

<template>
  <div class="container">
    <div class="header">
      <button>Filtern: asin</button>
      <button>Filtern: Titel</button>
      <button>Filtern: Kategorie</button>
      <button>Filtern: Rating</button>
      <button>getTrolls</button>
      <button>Finish</button>
    </div>

    <div>
      <h1>Auflistung der Produkte...</h1>
    </div>
  </div>
</template>

<style scoped>
  .container {
    display: flex;
    flex-direction: column;
  }

  .header {
    position: fixed;
    top: 20px;
    left: 20px;
    width: 100%;
    border-bottom: 1px solid #ccc; /* Optional: visual separation */
  }

</style>