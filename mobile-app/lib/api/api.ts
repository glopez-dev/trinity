import axios from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";

const excludeUrls = [
    '/auth/login',
    '/auth/register'
];

const isExcluded = (url: string | null) => {
    return excludeUrls.includes(url || '');
}

export const api = axios.create({
    baseURL: process.env.EXPO_PUBLIC_API_URL,
    timeout: 60000,
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(async (config) => {
    if (config?.url) {
        const token = await AsyncStorage.getItem("userToken");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
    }

    return config;
})