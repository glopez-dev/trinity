import { create } from 'zustand';
import AsyncStorage from "@react-native-async-storage/async-storage";
import { jwtDecode } from "jwt-decode";

interface User {
    id: string;
    name: string;
    email: string;
}

interface AuthState {
    user: User | null;
    loading: boolean;
    initialize: () => Promise<void>;
    logout: () => Promise<void>;
    isAuth: () => boolean;
}

export const useAuthStore = create<AuthState>((set, get) => ({
    user: null,
    loading: true,

    isAuth: () => {
        return get().user !== null;
    },

    initialize: async () => {
        try {
            const token = await AsyncStorage.getItem("userToken");
            if (token) {
                try {
                    const decodedUser: User = jwtDecode<User>(token);
                    set({ user: decodedUser });
                } catch (error) {
                    console.log("Token invalide");
                    await AsyncStorage.removeItem("userToken");
                }
            }
        } catch (error) {
            console.error("Erreur lors de l'initialisation de l'auth:", error);
        } finally {
            set({ loading: false });
        }
    },

    logout: async () => {
        await AsyncStorage.removeItem("userToken");
        set({ user: null });

    },
}));

export const initializeAuth = () => {
    useAuthStore.getState().initialize();
};