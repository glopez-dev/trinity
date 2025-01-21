import React from 'react';
import {ProductCard} from "@/components/ui/cards/product/ProductCard";
import styles from '@/styles/product/page.module.css';
import ProductCardSkeleton from "@/components/ui/cards/product/ProductCardSkeleton";
import {Product} from "@/lib/types/product/product";

interface ProductListProps {
    products: Product[];
    loading: boolean;
}

function ProductList({products, loading}: ProductListProps) {

    return (
        <div className={styles.productList}>
            {loading ?
                <>
                    <ProductCardSkeleton/>
                    <ProductCardSkeleton/>
                    <ProductCardSkeleton/>
                    <ProductCardSkeleton/>
                </>
                :
                products.map(product => (
                    <ProductCard key={product.id} product={product}/>
                ))
            }
        </div>
    );
}

export default ProductList;