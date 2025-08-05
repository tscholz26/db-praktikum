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