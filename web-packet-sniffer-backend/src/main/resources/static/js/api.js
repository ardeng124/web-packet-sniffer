const API_URL = "http://localhost:8080"

export const getAvailableInterfaces = async () => {
    try {
        const response = await fetch(`${API_URL}/api/interfaces`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching interfaces:', error);
        return [];
    }
}

export const startCapture = async (interfaceName) => {
    try {
        const res = await fetch(`${API_URL}/api/capture/start`, {
            method: "POST",
            headers: { "Content-Type": "text/plain" },
            body: interfaceName
        });
        const data = await res.json();
        if (!res.ok) {
            const error = new Error(data.message || "Failed to start capture");
            error.status = res.status;
            throw error;
        }

        return data;
    } catch (error) {
        console.error("Unable to start capture",error);
        throw error;
    }
}

export const getPackets = async () => {
    try {
        const response = await fetch(`${API_URL}/api/packets`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching packets:', error);
        return [];
    }
}

export const stopCapture = async() => {
    try {
        const res = await fetch(`${API_URL}/api/capture/stop`, {
            method: "POST",
            headers: { "Content-Type": "text/plain" },
        });
        const data = await res.json();
        if (!res.ok) {
            const error = new Error(data.message || "Failed to stop capture");
            error.status = res.status;
            throw error;
        }
        return data;
    } catch (error) {
        console.error("Unable to stop capture",error);
        throw error;
    }
}