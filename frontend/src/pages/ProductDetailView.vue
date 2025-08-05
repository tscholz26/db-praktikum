<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import {
  getProdukt,
  getRezensionen,
  getAngebote,
  getCategoryForItem,
  addRezensionApi
} from '../services/api.js';
import CategoryNode from '../components/CategoryNode.vue';


const route = useRoute();
const pnr = route.params.pnr;

const produkt = ref(null);
const rezensionen = ref([]);
const angebote = ref([]);
const loading = ref(true);
const kategorieTree = ref([]);

const newRezension = ref({
  nutzername: '',
  bewertung: '',
  rezension: ''
});

const submitRezension = async () => {
  if (!newRezension.value.nutzername || !newRezension.value.bewertung || !newRezension.value.rezension) {
    alert("Bitte alle Felder ausfüllen.");
    return;
  }

  try {
    const dto = {
      pnr: pnr,
      produktname: produkt.value?.titel || '',
      nutzername: newRezension.value.nutzername,
      bewertung: newRezension.value.bewertung,
      rezension: newRezension.value.rezension
    };

    await addRezensionApi(dto.pnr, dto.produktname, dto.nutzername, dto.bewertung, dto.rezension);

    alert("Rezension erfolgreich abgeschickt.");

  } catch (err) {
    console.error("Fehler beim Absenden der Rezension:", err);
    alert("Es gab einen Fehler beim Absenden der Rezension.");
  }
};

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

    <section class="rezension-form">
      <h2>Rezension schreiben</h2>
      <form @submit.prevent="submitRezension">
        <div class="form-group">
          <label for="nutzername">Name</label>
          <input id="nutzername" v-model="newRezension.nutzername" required />
        </div>

        <div class="form-group">
          <label for="bewertung">Bewertung (1-5)</label>
          <select id="bewertung" v-model.number="newRezension.bewertung" required>
            <option disabled value="">Bitte wählen</option>
            <option v-for="n in 5" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>

        <div class="form-group">
          <label for="rezension">Rezension</label>
          <textarea
              id="rezension"
              v-model="newRezension.rezension"
              rows="4"
              required
          ></textarea>
        </div>

        <button type="submit">Absenden</button>
      </form>
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

.rezension-form {
  margin-top: 2rem;
}

.rezension-form form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group label {
  font-weight: bold;
  margin-bottom: 0.25rem;
}

input, select, textarea {
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
}

button {
  align-self: flex-start;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  cursor: pointer;
}


</style>
