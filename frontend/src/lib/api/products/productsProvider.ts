import {api} from "@/lib/api/api";
import Cookies from "js-cookie";
import {ProductOFF, ProductResponse} from "@/lib/types/product/product";
import axios from "axios";

interface SearchProductResponse {
    products: ProductOFF[];
}

export const searchOFFProducts = async (productName: string | number): Promise<SearchProductResponse>  => {
    try {
        const token = Cookies.get('auth_token');

        const response = await api.post('/product/search', {
                'searchTerm': productName,
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
        );
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}

export const createProduct = async (product: ProductOFF): Promise<ProductResponse> => {
    try {
        const token = Cookies.get('auth_token');

        const response = await api.post('/product', product, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })

        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}