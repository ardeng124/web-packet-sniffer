export const getAvailableInterfaces = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/interfaces');
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching interfaces:', error);
        return [];
    }
}