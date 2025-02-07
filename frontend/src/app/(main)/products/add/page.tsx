'use client';

import React from 'react';
import styles from '@/styles/product/addPage.module.css';
import ProductForm from "@/components/forms/products/productForm";

export default function AddProductPage() {
    return (
        <div>
            <div className={styles.addProductHeader}>
                <h2>Ajouter un produit</h2>
            </div>
            <div className={styles.addProductBody}>
                <ProductForm />
            </div>
        </div>
    );
}