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

/*--------------------------------------------------------------
----GET MAPPINGS----GET MAPPINGS----GET MAPPINGS----GET MAPPINGS
--------------------------------------------------------------*/

// Get all persons
export const getPersons = async () => {
    try {
        const response = await apiClient.get(`${API_URL}/personen/all`);
        return response.data;
    } catch (error) {
        console.error("Error fetching persons:", error);
        throw error;
    }
};



export const getOrders = async (groupID,date) => {
    try {
        console.log("Getting orders for group with ID " + groupID + " and date " + date)
        const response = await fetch(`${API_URL}/bestellungen/group/${groupID}/date/${date}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            }
        });
        return response;
    } catch (error) {
        throw error;
    }
};


/*------------------------------------------------------------------
----POST MAPPINGS----POST MAPPINGS----POST MAPPINGS----POST MAPPINGS
------------------------------------------------------------------*/

export const addPerson = async (id, na, rl, gi, sn) => {
    try {
        const dataToSend = {
            personID: id,
            name: na,
            rolle: rl,
            gruppeID: gi,
            standortName: sn
        };
        console.log('Data to send: ', dataToSend);

        const response = await apiClient.post(`${API_URL}/personen/add`, dataToSend, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        // Handle the successful response if needed
        return response.data;
    } catch (error) {
        console.error('Error sending data to backend:', error);
        throw error;
    }
};


/*--------------------------------------------------------------
----PUT MAPPINGS----PUT MAPPINGS----PUT MAPPINGS----PUT MAPPINGS
--------------------------------------------------------------*/

export const updatePerson = async (id, na, rl, gi, sn) => {
    try {
        const dataToSend = {
            personID: id,
            name: na,
            rolle: rl,
            gruppeID: gi,
            standortName: sn
        };

        console.log('Sending data to backend:', dataToSend);

        const response = await apiClient.put(
            `${API_URL}/personen/update/${id}`, // ID in the URL
            dataToSend, // Payload
            { headers: { 'Content-Type': 'application/json' } }
        );
        return response.data;
    } catch (error) {
        console.error('Error sending data to backend:', error);
        throw error; // Let the caller handle errors
    }
};

