'use client'
import {useEffect, useState} from 'react';
import {ProductResponse} from "@/lib/types/product/product";
import styles from '@/styles/product/productDetail.module.css';
import {ProductCard} from "@/components/ui/cards/product/ProductCard";
import Button from "@/components/ui/buttons/button/Button";
import {useRouter} from "next/navigation";
import Modal from "@/components/ui/modal/Modal";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";
import {deleteProduct, getProductById} from "@/lib/api/products/productsProvider";
import ProductCardSkeleton from "@/components/ui/cards/product/ProductCardSkeleton";

export default function ProductDetailPage({params}: Readonly<{
    params: Promise<{ slug: string }>
}>) {
    const [slug, setSlug] = useState<string>('');
    const [product, setProduct] = useState<ProductResponse | null>(null);
    const [modalDeleteVisible, setModalDeleteVisible] = useState<boolean>(false)
    const {showMessage} = useFlash();
    const router = useRouter();

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

    const handleDelete = async () => {
        if (!product) {
            showMessage('error', 'Produit introuvable');
            return;
        }
        try {
            await deleteProduct(product.id);
            showMessage('success', 'Produit supprimé avec succès');
            router.push('/products');
        } catch (error) {
            if (error instanceof Error) {
                showMessage('error', error.message);
            } else {
                showMessage('error', 'Une erreur est survenue ! Veuillez réessayer plus tard.');
            }
        }
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
                {product ? (
                    <ProductCard product={product}/>
                ): (
                    <ProductCardSkeleton/>
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