import {ProductResponse, UpdateProductSchemaType} from "@/lib/types/product/product";
import styles from './updateProductForm.module.css';
import Input from "@/components/ui/input/input";
import React, {useState} from "react";
import Button from "@/components/ui/buttons/button/Button";
import Badge, {BadgeProps} from "@/components/ui/badge/Badge";
import {getStockStatus, STOCK_STATUS} from "@/lib/constants/products";
import Modal from "@/components/ui/modal/Modal";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";
import {updateProduct} from "@/lib/api/products/productsProvider";
import {useRouter} from "next/navigation";

export default function UpdateProductForm({product}: Readonly<{ product: ProductResponse }>) {
    const [payload, setPayload] = useState<UpdateProductSchemaType>({
        name: product.name,
        price: Number(product.price),
        quantity: 0
    });
    const [modalVisible, setModalVisible] = useState<boolean>(false);
    const {showMessage} = useFlash();
    const router = useRouter();

    const stockStatus = getStockStatus(Number(product.stock.quantity), Number(product.stock.minThreshold));
    const badgeData: BadgeProps = {
        text: stockStatus,
        type: 'success'
    }
    if (stockStatus === STOCK_STATUS.VERY_LOW_STOCK) {
        badgeData.type = 'error';
    } else if (stockStatus === STOCK_STATUS.LOW_STOCK) {
        badgeData.type = 'warning';
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            await updateProduct(payload, product.id);
            showMessage('success', 'Produit mis à jour avec succès');
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
        <form data-testid={'form-update'} onSubmit={handleSubmit} className={styles.formContainer}>
            <Input
                onChange={(value) => setPayload({...payload, name: value.toString()})}
                value={payload.name}
                name={'name'}
                label={'Nom'}
                required
            />
            <Input
                onChange={(value) => setPayload({...payload, price: Number(value)})}
                value={payload.price}
                name={'price'}
                label={'Prix'}
                type={'number'}
                required
            />
            <div className={styles.stockContainer}>
                <div className={styles.stockInfo}>
                    <p className={styles.stockTitle}>Stock</p>
                    <p>Seuil minimal : {product.stock.minThreshold}</p>
                    <div className={styles.currentQuantity}>
                        <p>Stock actuel : {product.stock.quantity}</p>
                        <Badge {...badgeData} />
                        <Button
                            title={'Ajouter du stock'}
                            action={() => setModalVisible(true)}
                            icon={'Plus'}
                        />
                    </div>
                    <p>Stock ajouté : {payload.quantity}</p>
                </div>
            </div>

            <Modal
                title={'Ajouter du stock'}
                body={<div>
                    <h4>Ajouter du stock :</h4>
                    <Input
                        onChange={(value) => setPayload({...payload, quantity: Number(value)})}
                        value={payload.quantity}
                        name={'quantity'}
                        type={'number'}
                        required
                    />
                </div>}
                footer={
                    <div className={styles.buttons}>
                        <Button
                            title={'Valider'}
                            action={() => setModalVisible(false)}
                            color={'primary'}
                        />
                        <Button
                            title={'Annuler'}
                            color={'secondary'}
                            action={() => {
                                setPayload({...payload, quantity: 0})
                                setModalVisible(false)
                            }}
                        />
                    </div>
                }
                modalVisible={modalVisible}
                setModalVisible={setModalVisible}
            />
            <Button title={'Enregistrer'} type={'submit'}/>
        </form>
    );
}