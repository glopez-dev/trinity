import {api} from "@/lib/api/api";
import Cookies from "js-cookie";
import {ProductResponse, UpdateProductSchemaType} from "@/lib/types/product/product";
import axios from "axios";

export const searchOFFProducts = async (productName: string | number): Promise<ProductResponse[]>  => {
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

export const createProduct = async (product: ProductResponse): Promise<ProductResponse> => {
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

export const getProducts = async (): Promise<ProductResponse[]> => {
    try {
        const token = Cookies.get('auth_token');

        const response = await api.get('/product', {
            headers: {
                'Authorization': `Bearer ${token}`
            },
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}

export const getProductById = async (productId: string): Promise<ProductResponse> => {
    try {
        const token = Cookies.get('auth_token');

        const response = await api.get(`/product/${productId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            },
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}

export const updateProduct = async (updateProduct: UpdateProductSchemaType, productId: string): Promise<ProductResponse> => {
    try {
        const token = Cookies.get('auth_token');

        const response = await api.put(`/product/${productId}`, updateProduct, {
            headers: {
                'Authorization': `Bearer ${token}`
            },
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}

export const deleteProduct = async (productId: string): Promise<void> => {
    try {
        const token = Cookies.get('auth_token');

        await api.delete(`/product/${productId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            },
        });
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}