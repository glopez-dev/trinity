'use client'

import styles from '@/styles/product/updateProduct.module.css'
import UpdateProductForm from "@/components/forms/products/updateProductForm";
import {useEffect, useState} from "react";
import {Product} from "@/lib/types/product/product";
import {products} from "@/lib/api/temporaryData/products/products";

export default function ProductUpdatePage({params}: Readonly<{
    params: Promise<{ slug: string }>
}>) {
    const [slug, setSlug] = useState<string>('');
    const [product, setProduct] = useState<Product | null>(null);

    useEffect(() => {
        params.then(result => {
            setSlug(result.slug);
        });
    }, [params]);

    useEffect(() => {
        products.filter(product => product.id === slug).forEach(product => setProduct(product));
    }, [slug]);

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2>Modifier ce produit</h2>
            </div>

            {product &&
                <UpdateProductForm product={product} />
            }
        </div>
    );
}