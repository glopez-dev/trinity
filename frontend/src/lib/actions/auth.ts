import {Login} from "@/lib/schemas/userSchema";
import {LoginResponse} from "@/lib/types/user/auth";
import {api} from "@/lib/api/api";
import axios from "axios";

export const login = async (formData: Login): Promise<LoginResponse | Error> => {
    try {
        const response = await api.post('/auth/login', formData);
        if (response.status !== 200) {
            throw new Error('Une erreur est survenue !');
        }
        return await response.data;
    } catch (e) {
        if(axios.isAxiosError(e)) {
            throw new Error(e.message);
        } else {
            throw new Error('Une erreur est survenue !')
        }
    }
};