<script setup>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  getProdukt,
  getRezensionen,
  getAngebote,
  getCategoryForItem,
  addRezensionApi,
  getCheaperSimilarProducts
} from '../services/api.js';
import CategoryNode from '../components/CategoryNode.vue';


const route = useRoute();
const router = useRouter();
const pnr = route.params.pnr;

const produkt = ref(null);
const rezensionen = ref([]);
const angebote = ref([]);
const similarCheaperProducts = ref([]);
const loading = ref(true);
const kategorieTree = ref([]);

const newRezension = ref({
  nutzername: '',
  bewertung: '',
  rezension: ''
});
const reviewAsGuest = ref(false);
const hoverRating = ref(0);

const submitRezension = async () => {
  if (!reviewAsGuest.value && !newRezension.value.nutzername) {
    alert("Bitte entweder einen Namen eingeben oder 'Als Gast bewerten' auswählen.");
    return;
  }

  if (!newRezension.value.bewertung || !newRezension.value.rezension) {
    alert("Bitte alle Felder ausfüllen.");
    return;
  }

  try {
    const dto = {
      pnr: pnr,
      produktname: produkt.value?.titel || '',
      nutzername: reviewAsGuest.value ? 'guest' : newRezension.value.nutzername,
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

const goToProduct = (pnr) => {
  router.push(`/shop/${pnr}`).then(() => {
    window.location.reload(); // forces full reload
  });
};


onMounted(async () => {
  try {
    produkt.value = await getProdukt(pnr);
    angebote.value = await getAngebote(pnr);
    kategorieTree.value = await getCategoryForItem(pnr);
    rezensionen.value = await getRezensionen(pnr);
    similarCheaperProducts.value = await getCheaperSimilarProducts(pnr);
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
      <p><strong>Verkaufsrang:</strong> {{ produkt.verkaufsrang ?? 'unbekannt'}}</p>
      <p><strong>Rating:</strong> {{ produkt.rating ?? 'Keine Bewertungen vorhanden' }}</p>
    </section>

    <section class="details">
      <h2>Detailinformationen</h2>
      <p>---Platzhalter---</p>
    </section>

    <section class="angebote">
      <h2>Angebote</h2>
      <table v-if="angebote.length">
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
          <td>{{ angebot.filialeName }}</td>
        </tr>
        </tbody>
      </table>
      <p v-else>Keine Angebote gefunden.</p>
    </section>

    <section class="similar-products">
      <h2>Ähnliche, günstigere Produkte</h2>
      <div  v-if="similarCheaperProducts.length" class="similar-list">
        <div
            class="similar-card"
            v-for="item in similarCheaperProducts"
            :key="item.pnr"
            @click="goToProduct(item.pnr)"
        >
          <img :src="item.bild" alt="Produktbild" />
          <h3>{{ item.titel }}</h3>
          <p><strong>PNR:</strong> {{ item.pnr }}</p>
          <p><strong>Rating:</strong> {{ item.rating ?? 'Keine Bewertung' }}</p>
        </div>
      </div>
      <div v-else>
        <p>Keine günstigeren ähnlichen Produkte gefunden.</p>
      </div>
    </section>

    <section class="rezension-form">
      <h2>Rezension schreiben</h2>
      <form @submit.prevent="submitRezension">

        <div class="form-group">
          <label>Bewertung</label>
          <div class="star-rating">
            <span
                v-for="n in 5"
                :key="n"
                class="star"
                :class="{ filled: n <= newRezension.bewertung }"
                @click="newRezension.bewertung = n"
                @mouseover="hoverRating = n"
                @mouseleave="hoverRating = 0"
            >
              ★
            </span>
          </div>
        </div>

        <div class="form-group name-group">
          <label for="nutzername">Nutzername</label>
          <div class="name-input-container">
            <input
                id="nutzername"
                v-model="newRezension.nutzername"
                :disabled="reviewAsGuest"
                placeholder="Ihr Nutzername"
                required
            />
            <label class="guest-label">
              <input
                  type="checkbox"
                  v-model="reviewAsGuest"
              />
              Als Gast bewerten
            </label>
          </div>
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

    <section class="rezensionen">
      <h2>Rezensionen anderer Kunden</h2>
      <div  v-if="rezensionen.length" v-for="rez in rezensionen" :key="rez.id" class="rezension">
        <div class="review">
          <span class="username">{{ rez.nutzername }}</span>
          <span class="star-rating">
            <span
                v-for="n in 5"
                :key="n"
                class="star-small"
                :class="{ filled: n <= rez.bewertung }"
            >★</span>
          </span>
        </div>
        <p v-html="rez.rezension"></p>
      </div>
      <div v-else>
        <p>Keine Rezensionen gefunden.</p>
      </div>
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

.name-input-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.guest-label {
  font-weight: normal;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
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

.star-rating {
  font-size: 2rem;
  color: #ddd;
  display: flex;
  gap: 0.25rem;
  cursor: pointer;
}

.star-rating .star.filled,
.star-rating .star:hover,
.star-rating .star:hover ~ .star {
  color: gold;
}

.star-rating .star {
  transition: color 0.2s;
}

.star-rating .star:hover ~ .star {
  color: #ddd !important;
}

.similar-products {
  margin-top: 2rem;
}

.similar-list {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.similar-card {
  cursor: pointer;
  border: 1px solid #ddd;
  padding: 1rem;
  width: 180px;
  text-align: center;
  transition: transform 0.2s ease;
}

.similar-card:hover {
  transform: scale(1.05);
  border-color: #aaa;
}

.similar-card img {
  max-width: 100%;
  height: auto;
  margin-bottom: 0.5rem;
}

h2 {
  margin-top: 2rem;
  font-weight: bold;
}
.review {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0.5rem 0;
  font-size: 1rem;
}

.username {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.2rem;
}

.star-rating {
  display: flex;
}

.star-small {
  color: #ccc; /* empty star */
  font-size: 1.2rem;
}

.star-small.filled {
  color: #f5c518; /* filled star (gold) */
}

</style>
