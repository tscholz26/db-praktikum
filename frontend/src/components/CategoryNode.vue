<template>
  <li>
    {{ node.kategorienName }}
    <ul v-if="node.children && node.children.length">
      <CategoryNode
          v-for="child in node.children"
          :key="child.kategorieid"
          :node="child"
      />
    </ul>
  </li>
</template>

<script setup>
defineProps({
  node: {
    type: Object,
    required: true
  }
});
</script>

<style scoped>
li {
  margin-top: 0.25rem;
}
ul {
  padding-left: 1rem;
  list-style-type: disc;
}
</style>
