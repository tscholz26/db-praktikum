import axios from 'axios';

// Set the base URL for your API
const API_URL = import.meta.env.VITE_BACKEND_URL;

// Create an axios instance with default configurations
const apiClient = axios.create({
    baseURL: API_URL,
    //headers: {
    //    'Content-Type': 'application/json',
    //},
});

// Hello World
export const getTestMessage = async () => {
    try {
        const response = await apiClient.get(`${API_URL}/test`);
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

export function getRezensionen(pnr) {
    return axios.get('/getRezensionen', { params: { pnr } });
}

export function addRezension(rezension) {
    return axios.post('/addRezension', rezension);
}
