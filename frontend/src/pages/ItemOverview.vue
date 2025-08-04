<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProdukte } from '../services/api.js'

const router = useRouter()
const produkte = ref([])
const pattern = ref('')
const asinInput = ref('')
const titleInput = ref('')

const fetchProdukte = async () => {
  try {
    const data = await getProdukte(pattern.value)
    produkte.value = data
  } catch (error) {
    console.error('Fehler beim Laden der Produkte:', error)
    produkte.value = []
  }
}

onMounted(fetchProdukte)

const goToDetail = (pnr) => {
  router.push(`/shop/${pnr}`)
}

const handleAsinFilter = () => {
  if (asinInput.value.trim()) {
    router.push(`/shop/${asinInput.value.trim()}`)
  }
}

const handleTitleFilter = async () => {
  pattern.value = titleInput.value.trim()
  await fetchProdukte()
}

// Titel kürzen
const truncateTitle = (title) => {
  if (!title) return ''
  return title.length > 50 ? title.slice(0, 47) + '...' : title
}
</script>

<template>
  <div class="page-wrapper">
    <header class="header">
      <div class="filter-group">
        <input v-model="asinInput" placeholder="PNR eingeben" />
        <button @click="handleAsinFilter">Filter: asin</button>
      </div>

      <div class="filter-group">
        <input v-model="titleInput" placeholder="Titel eingeben" />
        <button @click="handleTitleFilter">Filter: title</button>
      </div>

      <button disabled>Filter: category</button>
      <button disabled>Filter: Rating</button>
      <button disabled>getTrolls</button>
      <button disabled>Finish</button>
    </header>

    <main class="content">
      <div
          class="product-list"
          v-if="produkte.length"
      >
        <div
            class="product-item"
            v-for="produkt in produkte"
            :key="produkt.pnr"
            @click="goToDetail(produkt.pnr)"
        >
          <div class="image-wrapper">
            <img
                v-if="produkt.bild && produkt.bild.trim() !== ''"
                :src="produkt.bild"
                :alt="produkt.titel"
                class="product-image"
                loading="lazy"
            />
            <div v-else class="placeholder-image" aria-label="Kein Bild verfügbar">
              🖼️
            </div>
          </div>
          <h3>{{ truncateTitle(produkt.titel) }}</h3>
          <p>PNR: {{ produkt.pnr }}</p>
          <p>Verkaufsrang: {{ produkt.verkaufsrang ?? 'unbekannt' }}</p>
          <p>Rating: {{ produkt.rating ?? 'unbekannt' }}</p>
        </div>
      </div>
      <p v-else class="no-products">Keine Produkte gefunden.</p>
    </main>
  </div>
</template>

<style scoped>
.page-wrapper {
  width: 90vw;
  margin-left: auto;
  margin-right: auto;
  padding-left: 2rem;
  padding-right: 2rem;
  box-sizing: border-box;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: sticky;
  top: 0;
  background-color: #fff;
  padding: 1rem 0;
  border-bottom: 1px solid #ccc;
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  align-items: center;
  z-index: 100;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.filter-group input {
  padding: 0.4rem 0.6rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  min-width: 180px;
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

button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.content {
  margin-top: 1rem;
  flex-grow: 1;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.2rem;
}

.product-item {
  background-color: #fafafa;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  transition: box-shadow 0.2s ease, background-color 0.2s ease;
}

.product-item:hover {
  background-color: #f0f0f0;
  box-shadow: 0 0 8px rgba(0,0,0,0.1);
}

.image-wrapper {
  width: 100%;
  max-height: 160px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 0.8rem;
  border-radius: 6px;
  overflow: hidden;
}

.product-image {
  max-width: 100%;
  max-height: 160px;
  object-fit: contain;
  border-radius: 6px;
}

.placeholder-image {
  font-size: 4rem;
  color: #ccc;
  user-select: none;
}

.no-products {
  font-size: 1.25rem;
  text-align: center;
  margin-top: 2rem;
  color: #666;
}
</style>
