<script setup>
import { ref } from 'vue'
import { getTrolls } from '../services/api.js'

const threshold = ref('')
const trolls = ref([])

const printTrolls = async () => {
  try {
    const data = await getTrolls(threshold.value)
    trolls.value = data
  } catch (error) {
    console.error('Fehler beim Laden der Trolls:', error)
  }
}
</script>

<template>
  <div class="container">
    <h1 class="title">Troll-Kunden mit niedrigen Bewertungen</h1>

    <div class="input-group">
      <input
          v-model="threshold"
          type="number"
          placeholder="Grenzwert eingeben (z. B. 2.5)"
          class="input"
      />
      <button @click="printTrolls" class="btn">Trolls anzeigen</button>
    </div>

    <table v-if="trolls.length" class="troll-table">
      <thead>
      <tr>
        <th>Nutzername</th>
        <th>Durchschnitts-Rating</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="troll in trolls">
        <td>{{ troll.nutzername }}</td>
        <td>{{ troll.durchschnittsRating.toFixed(2) }}
        </td>
      </tr>
      </tbody>
    </table>

    <p v-else class="no-results">Keine Troll-Kunden gefunden.</p>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

.title {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #343a40;
}

.input-group {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.input {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border: 1px solid #ced4da;
  border-radius: 6px;
  width: 200px;
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



.troll-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.troll-table th,
.troll-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #dee2e6;
}

.troll-table th {
  background-color: #e9ecef;
  color: #495057;
}

.no-results {
  text-align: center;
  color: #6c757d;
  font-style: italic;
}
</style>
