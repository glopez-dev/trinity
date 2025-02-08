import {Product} from "@/lib/types/product/product";
import styles from './updateProductForm.module.css';
import Input from "@/components/ui/input/input";
import React, {useState} from "react";
import Button from "@/components/ui/buttons/button/Button";
import Badge, {BadgeProps} from "@/components/ui/badge/Badge";
import {getStockStatus, STOCK_STATUS} from "@/lib/constants/products";
import Modal from "@/components/ui/modal/Modal";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";

interface UpdateProductPayload {
    name: string;
    price: number;
    currentQuantity: number;
}

export default function UpdateProductForm({product}: Readonly<{ product: Product }>) {
    const [payload, setPayload] = useState<UpdateProductPayload>({
        name: product.name,
        price: product.price,
        currentQuantity: 0
    });
    const [modalVisible, setModalVisible] = useState<boolean>(false);
    const {showMessage} = useFlash();

    const stockStatus = getStockStatus(product.stock.currentQuantity, product.stock.minThreshold);
    const badgeData: BadgeProps = {
        text: stockStatus,
        type: 'success'
    }
    if (stockStatus === STOCK_STATUS.VERY_LOW_STOCK) {
        badgeData.type = 'error';
    } else if (stockStatus === STOCK_STATUS.LOW_STOCK) {
        badgeData.type = 'warning';
    }

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        showMessage('success', 'Produit mis à jour avec succès');
        console.log(payload);
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
                        <p>Stock actuel : {product.stock.currentQuantity}</p>
                        <Badge {...badgeData} />
                        <Button
                            title={'Ajouter du stock'}
                            action={() => setModalVisible(true)}
                            icon={'Plus'}
                        />
                    </div>
                    <p>Stock ajouté : {payload.currentQuantity}</p>
                </div>
            </div>

            <Modal
                title={'Ajouter du stock'}
                body={<div>
                    <h4>Ajouter du stock :</h4>
                    <Input
                        onChange={(value) => setPayload({...payload, currentQuantity: Number(value)})}
                        value={payload.currentQuantity}
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
                                setPayload({...payload, currentQuantity: 0})
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