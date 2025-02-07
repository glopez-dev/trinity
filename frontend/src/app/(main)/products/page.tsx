'use client';

import {useEffect, useState} from "react";
import {products as p} from "@/lib/api/temporaryData/products/products";
import styles from "@/styles/product/page.module.css";
import ProductList from "@/components/features/product/ProductList";
import Input from "@/components/ui/input/input";
import {InputValueTypes} from "@/components/ui/input/types";
import Button from "@/components/ui/buttons/button/Button";
import IconButton from "@/components/ui/buttons/icon-button/IconButton";
import {ProductSearch} from "@/lib/schemas/productSchema";
import {Product} from "@/lib/types/product/product";
import {useRouter} from "next/navigation";

export default function Products() {

    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [searchFilter, setSearchFilter] = useState<ProductSearch>({
        name: ''
    });
    const router = useRouter();

    const handleSearch = (value: InputValueTypes) => {
        setSearchFilter({
            name: value.toString()
        });
        const filteredProducts = p.filter(product => product.name.toLowerCase().includes(value.toString().toLowerCase()));
        setProducts(filteredProducts);
    }

    const handleRefresh = () => {
        setLoading(true);
        setTimeout(() => {
            setLoading(false);
        }, 2000);
    }

    useEffect(() => {
        setProducts(p);
    }, [loading]);

    return (
        <div className={styles.productContainer}>
            <div className={styles.productContainerHeader}>
                <h1>Liste des produits</h1>
            </div>
            <div className={styles.actionContainer}>
                <div className={styles.filtersContainer}>
                    <Input
                        name={'name'}
                        type={'text'}
                        placeholder={'Rechercher un produit'}
                        value={searchFilter.name}
                        onChange={(value) => handleSearch(value)}
                    />
                    <IconButton icon={'Filter'} onClick={() => console.log('test')}/>
                </div>
                <div className={styles.addContainer}>
                    <Button title={'Ajouter'} icon={'Plus'} action={() => router.replace('/products/add')}/>
                    <IconButton icon={'RefreshCw'} onClick={handleRefresh} />
                </div>
            </div>
            <ProductList products={products} loading={loading}/>
        </div>
    );
}