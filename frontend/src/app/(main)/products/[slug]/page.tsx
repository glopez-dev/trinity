'use client'
import {useEffect, useState} from 'react';
import {Product} from "@/lib/types/product/product";
import {products} from "@/lib/api/temporaryData/products/products";
import styles from '@/styles/product/productDetail.module.css';
import {ProductCard} from "@/components/ui/cards/product/ProductCard";
import Button from "@/components/ui/buttons/button/Button";
import {useRouter} from "next/navigation";
import Modal from "@/components/ui/modal/Modal";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";

export default function ProductDetailPage({params}: Readonly<{
    params: Promise<{ slug: string }>
}>) {
    const [slug, setSlug] = useState<string>('');
    const [product, setProduct] = useState<Product | null>(null);
    const [modalDeleteVisible, setModalDeleteVisible] = useState<boolean>(false)
    const {showMessage} = useFlash();
    const router = useRouter();

    useEffect(() => {
        params.then(result => {
            setSlug(result.slug);
        });
    }, [params]);

    useEffect(() => {
        products.filter(product => product.id === slug).forEach(product => setProduct(product));
    }, [slug]);

    function handleDelete() {
        showMessage('success', 'Produit supprimé avec succès');
        router.push('/products');
    }

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2>Fiche Produit</h2>
                <div className={styles.buttons}>
                    <Button
                        title={'Modifier'}
                        action={() => router.push(`/products/${slug}/update`)}
                        icon={'Pencil'}
                    />
                    <Button
                        title={'Supprimer'}
                        action={() => setModalDeleteVisible(true)}
                        icon={'Trash'}
                        color={'accent'}
                    />
                </div>
            </div>
            <div className={styles.productContainer}>
                {product && (
                    <ProductCard product={product}/>
                )}
            </div>
            <Modal
                title={'Supprimer ce produit'}
                body={<h4>Êtes-vous sur de bien vouloir supprimer ce produit ?</h4>}
                footer={<div className={styles.buttons}>
                    <Button
                        title={'Annuler'}
                        action={() => setModalDeleteVisible(false)}
                        color={'secondary'}
                    />
                    <Button
                        title={'Supprimer'}
                        action={handleDelete}
                        color={'accent'}
                    />
                </div>
                }
                modalVisible={modalDeleteVisible}
                setModalVisible={setModalDeleteVisible}

            />
        </div>
    );
}