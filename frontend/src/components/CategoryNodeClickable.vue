<script setup>
import { computed } from 'vue'

const { node, expanded } = defineProps({
  node: Object,     //kategorie (elternknoten), wird durch Name, ID, Children Kategorisiert
  expanded: Object  //trackt welche kategorien gerade ausgeklappt sind (= Auflistung der IDs der ausgeklappten Knoten)
})

const isExpanded = computed(() => expanded.has(node.kategorieid)) //trackt, ob dieser knoten selbst gerade ausgeklappt ist
</script>

<template>
  <li>  <!-- Listeneintrag, der pro Knoten gerendert wird-->
    <div class="category-node">
      <!-- Zeige ausklappoption falls die node kinder hat -->
      <span
          v-if="node.children && node.children.length"
          @click="$emit('toggle', node.kategorieid)"
          class="expand-toggle"
      >
        {{ isExpanded ? '▽' : '▷' }}
      </span>

      <!-- eigentlicher kategoriename, bei klicken wird select-event ausgewählt -->
      <span class="category-name" @click="$emit('select', node.kategorieid)">
        {{ node.kategorienName }}
      </span>
    </div>

    <ul v-if="isExpanded && node.children?.length" class="category-list nested">
      <!-- wenn ausgeklappt und es gibt kinder: rekursiv weitere CategoryNodes für child kategorien rendern-->
      <CategoryNodeClickable
          v-for="child in node.children"
          :key="child.kategorieid"
          :node="child"
          :expanded="expanded"
          @toggle="$emit('toggle', $event)"
          @select="$emit('select', $event)"
      />
    </ul>
  </li>
</template>

<style scoped>
ul {
  list-style-type: none;
}

.category-name:hover {
  text-decoration: underline;
  cursor: pointer;
  //font-weight: bold;
  color: darkblue;
}

</style>
