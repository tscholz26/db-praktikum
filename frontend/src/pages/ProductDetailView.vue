<template>
  <div class="product-detail">
    <section v-if="produkt" class="product-info">
      <h1>{{ produkt.titel }}</h1>
      <img :src="produkt.bild" alt="Produktbild" />
      <p><strong>PNR:</strong> {{ produkt.pnr }}</p>
      <p><strong>Verkaufsrang:</strong> {{ produkt.verkaufsrang }}</p>
      <p><strong>Rating:</strong> {{ produkt.rating ?? 'Keine Bewertung' }}</p>
    </section>

    <section v-if="rezensionen.length" class="rezensionen">
      <h2>Rezensionen</h2>
      <div v-for="rez in rezensionen" :key="rez.id" class="rezension">
        <p><strong>{{ rez.nutzername }}</strong> ({{ rez.bewertung }}/5)</p>
        <p v-html="rez.rezension"></p>
      </div>
    </section>

    <section v-if="angebote.length" class="angebote">
      <h2>Angebote</h2>
      <table>
        <thead>
        <tr>
          <th>Preis</th>
          <th>Zustand</th>
          <th>Filiale</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="angebot in angebote" :key="angebot.id">
          <td>{{ angebot.preis }} {{angebot.waehrung }}</td>
          <td>{{ angebot.zustand }}</td>
          <td>{{ angebot.filialeid }}</td>
        </tr>
        </tbody>
      </table>
    </section>

    <section v-if="kategorieTree.length" class="kategorien">
      <h2>Kategorien</h2>
      <ul>
        <CategoryNode v-for="node in kategorieTree" :key="node.kategorieid" :node="node" />
      </ul>
    </section>

    <section v-if="!produkt && !loading">
      <p>Produkt nicht gefunden.</p>
    </section>
  </div>
</template>

<script setup>
  import { onMounted, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import {
    getProdukt,
    getRezensionen,
    getAngebote,
    getCategoryForItem
  } from '../services/api.js';
  import CategoryNode from '../components/CategoryNode.vue';


  const route = useRoute();
  const pnr = route.params.pnr;

  const produkt = ref(null);
  const rezensionen = ref([]);
  const angebote = ref([]);
  const loading = ref(true);
  const kategorieTree = ref([]);

  onMounted(async () => {
    try {
      produkt.value = await getProdukt(pnr);
      rezensionen.value = await getRezensionen(pnr);
      angebote.value = await getAngebote(pnr);
      kategorieTree.value = await getCategoryForItem(pnr);
    } catch (error) {
      console.error("Fehler beim Laden der Produktdaten:", error);
    } finally {
      loading.value = false;
    }
  });
</script>

<style scoped>
.product-detail {
  padding: 1rem;
  max-width: 800px;
  margin: auto;
}

.product-info img {
  max-width: 200px;
  margin-bottom: 1rem;
}

.rezensionen, .angebote {
  margin-top: 2rem;
}

.rezension {
  border-bottom: 1px solid #ccc;
  padding: 0.5rem 0;
}

.angebote table {
  width: 100%;
  border-collapse: collapse;
}

.angebote th, .angebote td {
  padding: 0.5rem;
  border: 1px solid #ddd;
  text-align: left;
}
</style>
