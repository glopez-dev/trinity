import React, {useState} from 'react'
import styles from './productForm.module.css';
import Input from "@/components/ui/input/input";
import {InputValueTypes} from "@/components/ui/input/types";
import IconButton from "@/components/ui/buttons/icon-button/IconButton";
import {createProduct, searchOFFProducts} from "@/lib/api/products/productsProvider";
import {ProductOFF} from "@/lib/types/product/product";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";
import AddProductCard from "@/components/ui/cards/product/AddProductCard";
import axios from "axios";
import Button from "@/components/ui/buttons/button/Button";
import {useRouter} from "next/navigation";

export default function ProductForm() {
    const [selectedProduct, setSelectedProduct] = useState<ProductOFF | null>(null);
    const [currentQuantity, setCurrentQuantity] = useState<InputValueTypes>('');
    const [minThreshold, setMinThreshold] = useState<InputValueTypes>('');
    const [tooltipProduct, setTooltipProduct] = useState<string | null>(null);
    const [searchValue, setSearchValue] = useState<InputValueTypes>('');
    const [searchResults, setSearchResults] = useState<ProductOFF[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const router = useRouter();
    const {showMessage} = useFlash();

    const handleSearch = async () => {
        if (searchValue === '') {
            showMessage('warning', 'Veuillez entrer un nom de produit à rechercher');
            return;
        }
        try {
            setSearchResults([]);
            setLoading(true);
            const response = await searchOFFProducts(searchValue);
            setSearchResults(response.products);
            setLoading(false);
        } catch (e) {
            if (axios.isAxiosError(e)) {
                showMessage('error', e.message);
            } else {
                showMessage('error', 'Une erreur est survenue ! Veuillez réessayer plus tard.');
            }
            setLoading(false);
        }
    }

    const handleSubmit = async () => {
        if (!selectedProduct || currentQuantity === '' || minThreshold === '') {
            showMessage('warning', 'Veuillez remplir tous les champs');
        } else {
            const newProduct = {
                ...selectedProduct,
                stock: {
                    currentQuantity: currentQuantity,
                    minThreshold: minThreshold,
                    maxThreshold: '10000',
                }
            }

            setSelectedProduct(newProduct);

            try {
                const response = await createProduct(newProduct);
                if (response.id !== null && response.id !== undefined) {
                    setSelectedProduct(null);
                    setCurrentQuantity('');
                    setMinThreshold('');
                    router.push('/products');
                    showMessage('success', 'Produit ajouté avec succès');
                }
            } catch (e) {
                if (axios.isAxiosError(e)) {
                    showMessage('error', e.message);
                } else {
                    showMessage('error', 'Une erreur est survenue ! Veuillez réessayer plus tard.');
                }
            }
        }
    }

    return (
        <div className={styles.productFormContainer}>
            <div className={styles.productFormHeader}>
                <Input
                    name={'name'}
                    type={'text'}
                    placeholder={'Rechercher un produit dans le catalogue'}
                    value={searchValue}
                    onChange={(value) => setSearchValue(value)}
                />
                <IconButton
                    onClick={handleSearch}
                    icon={'Search'}
                />
            </div>
            <div className={styles.productFormBody}>
                {loading &&
                    <div className={styles.searchSkeleton}>
                        <div className={styles.skeletonTitle}></div>
                        <div className={styles.skeletonCard}></div>
                    </div>
                }

                {searchResults.length > 0 && (
                    <h3>Selectionnez le produit à ajouter</h3>
                )}
                <div className={styles.productGrid}>
                    {searchResults.length > 0 && searchResults.map((product: ProductOFF, index: number) => {
                        return (
                            <AddProductCard
                                key={product.stock ? product.stock.currentQuantity : index}
                                product={product}
                                isSelected={selectedProduct?.barcode === product.barcode}
                                onSelect={(product) => setSelectedProduct(product)}
                                onMouseEnter={(product) => setTooltipProduct(product?.ingredients)}
                                onMouseLeave={() => setTooltipProduct(null)}
                                tooltip={tooltipProduct}
                            />
                        )
                    })}
                </div>

                {selectedProduct && (
                    <div className={styles.inputContainer}>
                        <Input
                            onChange={(value) => setSelectedProduct({
                                ...selectedProduct,
                                name: value.toString()
                            })}
                            value={selectedProduct.name ? selectedProduct.name : ''}
                            name={'nom'}
                            placeholder={'Nom du produit...'}
                            label={'Nom'}
                            required
                        />
                        <Input
                            onChange={(value) => setSelectedProduct({
                                ...selectedProduct,
                                price: value
                            })}
                            value={selectedProduct.price ? selectedProduct.price : ''}
                            name={'price'}
                            type={'number'}
                            placeholder={'Prix en €...'}
                            label={'Prix'}
                            required
                        />
                        <Input
                            onChange={(value) => setCurrentQuantity(value)}
                            value={currentQuantity}
                            name={'quantity'}
                            type={'number'}
                            placeholder={'Quantité...'}
                            label={'Quantité'}
                            required
                        />
                        <Input
                            onChange={(value) => setMinThreshold(value)}
                            value={minThreshold}
                            name={'minThreshold'}
                            type={'number'}
                            placeholder={'Seuil minimum...'}
                            label={'Seuil minimum'}
                            required
                        />
                        <Button
                            title={'Ajouter'}
                            action={() => handleSubmit()}
                            size={'full'}
                            color={'primary'}
                            type={'button'}
                        />
                    </div>
                )}
            </div>

        </div>
    )
}