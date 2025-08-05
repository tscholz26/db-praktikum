import axios from 'axios';

// Set the base URL for your API
const API_URL = import.meta.env.VITE_BACKEND_URL;

// Create an axios instance with default configurations
const apiClient = axios.create({
    baseURL: API_URL,
});

// Init DB Connection
export const initDB = async () => {
    try {
        const response = await apiClient.post('/init');
        return response.data;
    } catch (error) {
        console.error("Error connecting to DB:", error);
        throw error;
    }
};

// Finish DB Connection
export const finishDB = async () => {
    try {
        const response = await apiClient.post('/finish');
        return response.data;
    } catch (error) {
        console.error("Error terminating connection to DB:", error);
        throw error;
    }
};

// Hello World
export const getTestMessage = async () => {
    try {
        const response = await apiClient.get('/test');
        return response.data;
    } catch (error) {
        console.error("Error fetching Hello World:", error);
        throw error;
    }
};

// Get list of products (optionally with pattern filter)
export const getProdukte = async (pattern = '') => {
    try {
        const response = await apiClient.get('/getProdukte', {
            params: pattern ? { pattern } : {}
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching products:", error);
        throw error;
    }
};

export const getProductsByCategoryPath = async (path) => {
    console.log("Filtering products by category path:", path);
    //TODO: USE ACTUAL CATEGORY FILTER HERE
    const pattern = path ? path : '';
    try {
        const response = await apiClient.get('/getProdukte');
        return response.data;
    } catch (error) {
        console.error("Error fetching products by category path:", error);
        throw error;
    }
};

// Get single product by PNR
export const getProdukt = async (pnr) => {
    try {
        const response = await apiClient.get('/getProdukt', {
            params: { pnr }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching product:", error);
        throw error;
    }
};

// Get reviews for a product
export const getRezensionen = async (pnr) => {
    try {
        const response = await apiClient.get('/getRezensionen', {
            params: { pnr }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching reviews:", error);
        throw error;
    }
};

// Add a new review
export const addRezension = async (rezension) => {
    try {
        const response = await apiClient.post('/addRezension', rezension);
        return response.data;
    } catch (error) {
        console.error("Error adding review:", error);
        throw error;
    }
};

// Get offers for a product
export const getAngebote = async (pnr) => {
    try {
        const response = await apiClient.get('/getAngebote', {
            params: { pnr }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching offers:", error);
        throw error;
    }
};

export const getTopProdukte = async (lim) => {
    try {
        //const response = await apiClient.get('/getTopProdukte', {
        //    params: { lim }
        //});
        const response = await apiClient.get('/getTopProdukte', {
            params: { lim: lim }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching top products:", error);
        throw error;
    }
};

export const getCategoryForItem = async (pnr) => {
    try {
        const response = await apiClient.get('/getKategorieBaum', {
            params: { pnr }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching category tree for item:", error);
        throw error;
    }
};


export const getCheaperSimilarProducts = async (pnr) => {
    try {
        const response = await apiClient.get('/getBilligereProdukte', {
            params: { pnr }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching cheaper similar products for item:", error);
        throw error;
    }
};

export const getTrolls = async (threshold) => {
    try {
        const response = await apiClient.get('/TrollsKunden', {
            params: { grenzwertRating: threshold  }
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching trolls", error);
        throw error;
    }
};

export async function addRezensionApi(dto_pnr, dto_produktname, dto_nutzername, dto_bewertung, dto_rezension) {
    try {
        const dataToSend = {
            pnr: dto_pnr,
            produktname: dto_produktname,
            nutzername: dto_nutzername,
            bewertung: dto_bewertung,
            rezension: dto_rezension
        };

        const response = await apiClient.post(`${API_URL}/addRezension`, dataToSend, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        // Handle the successful response if needed
        return response.data;
    } catch (error) {
        console.error('Error adding rezension:', error);
        throw error;
    }
};