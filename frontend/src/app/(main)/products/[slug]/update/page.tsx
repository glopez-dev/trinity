'use client'

import styles from '@/styles/product/updateProduct.module.css'
import UpdateProductForm from "@/components/forms/products/updateProductForm";
import {useEffect, useState} from "react";
import {ProductResponse} from "@/lib/types/product/product";
import {getProductById} from "@/lib/api/products/productsProvider";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";

export default function ProductUpdatePage({params}: Readonly<{
    params: Promise<{ slug: string }>
}>) {
    const [slug, setSlug] = useState<string>('');
    const [product, setProduct] = useState<ProductResponse | null>(null);
    const {showMessage} = useFlash();

    useEffect(() => {
        params.then(result => {
            setSlug(result.slug);
        });
    }, [params]);

    useEffect(() => {
        const getProductBySlug = async () => {
            try {
                const response = await getProductById(slug);
                setProduct(response);
            } catch (error) {
                if (error instanceof Error) {
                    showMessage('error', error.message);
                } else {
                    showMessage('error', 'Une erreur est survenue ! Veuillez réessayer plus tard.');
                }
            }
        }
        if (slug) {
            getProductBySlug().then(r => console.log(r));
        }
    }, [showMessage, slug]);

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2>Modifier ce produit</h2>
            </div>

            {product &&
                <UpdateProductForm product={product}/>
            }
        </div>
    );
}