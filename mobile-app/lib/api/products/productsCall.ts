import {api} from "@/lib/api/api";
import axios from "axios";


const getProductByBarcode = async ({barcode}: Readonly<GetProductByBarcodeProps>) =>  {
    try {
        const response = await api.get(`/product/${barcode}`);
        if (response.status === 200) {
            console.log(response)
        }
    } catch (error) {
        console.error("Erreur lors de la récupération du produit:", error);
    }
}

export const getProducts = async () => {
    try {
        const response = await api.get('/product');
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.message);
        } else {
            throw new Error('Une erreur est survenue ! Veuillez réessayer plus tard.');
        }
    }
}