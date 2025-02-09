'use client';

import {useEffect, useState} from "react";
import Button from "@/components/ui/buttons/button/Button";
import styles from '@/styles/invoices/invoiceDetailPage.module.css';
import {Invoice} from "@/lib/types/invoices/invoices";
import {sampleInvoices} from "@/lib/api/temporaryData/invoices/invoices";
import {CANCELLED, getInvoiceStatusBadge, InvoiceStatus, PAID, PENDING} from "@/lib/constants/invoices";
import Modal from "@/components/ui/modal/Modal";
import SearchableSelect from "@/components/ui/input/select/SearchableSelect";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";

export default function InvoiceDetailPage({params}: Readonly<Readonly<{
    params: Promise<{ id: string }>
}>>) {
    const [id, setId] = useState<string | null>(null);
    const [invoice, setInvoice] = useState<Invoice | null>(null);
    const [editModalVisible, setEditModalVisible] = useState<boolean>(false);
    const [deleteModalVisible, setDeleteModalVisible] = useState<boolean>(false);
    const [newStatus, setNewStatus] = useState<string | null>(null);
    const {showMessage} = useFlash();


    useEffect(() => {
        params.then(result => {
            setId(result.id);
        });
    }, [params]);


    useEffect(() => {
        sampleInvoices
            .filter(invoice => invoice.id === id)
            .forEach(invoice => setInvoice(invoice));
    }, [id]);

    if (!invoice) {
        return null;
    }

    const handleEdit = () => {
        showMessage('success', 'Facture modifiée avec succès');
        console.log(newStatus)
        setEditModalVisible(false);
    }

    const handleDelete = () => {
        showMessage('success', 'Facture supprimée avec succès');
        setDeleteModalVisible(false);
    }

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2>Facture n° {invoice.id}</h2>
                <div className={styles.buttonContainer}>
                    <Button title={'Modifier'} action={() => setEditModalVisible(true)} icon={'Pencil'}/>
                    <Button title={'Supprimer'} action={() => setDeleteModalVisible(true)} icon={'Trash'}
                            color={'accent'}/>
                </div>
            </div>
            <div className={styles.invoiceContainer}>
                <div className={styles.invoiceContent}>
                    <h3>Contenu de la facture</h3>
                    <div className={styles.invoiceItems}>
                        {invoice.items.map((item) => (
                            <div key={item.name} className={styles.invoiceItem}>
                                <span>
                                    <p className={styles.productName}>{item.name} </p>
                                    <p>x{item.quantity}</p>
                                </span>
                                <span>
                                    <p>{item.quantity} x {item.unitPrice} {invoice.totalAmount.currency}</p>
                                </span>
                            </div>
                        ))}
                    </div>
                    <div className={styles.invoiceTotal}>
                        <span className={styles.invoiceItem}>
                            <p className={styles.productName}>Coût total de la commande</p>
                            <p>{invoice.totalAmount.value} {invoice.totalAmount.currency}</p>
                        </span>
                    </div>
                </div>
                <div className={styles.invoiceDetails}>
                    <p>Client: {invoice.billingInfo.firstName} {invoice.billingInfo.lastName}</p>
                    <span>Statut: {getInvoiceStatusBadge(invoice.status)}</span>
                </div>
            </div>
            <Modal
                title={'Supprimer cette facture'}
                body={<h4>
                    Êtes-vous sur de bien vouloir supprimer cette facture ?
                </h4>}
                footer={<div className={styles.buttons}>
                    <Button title={'Annuler'} action={() => setDeleteModalVisible(false)} color={'secondary'}/>
                    <Button title={'Supprimer'} action={handleDelete} color={'accent'}/>
                </div>}
                modalVisible={deleteModalVisible}
                setModalVisible={setDeleteModalVisible}
            />
            <Modal
                title={'Modifier cette facture'}
                body={
                    <div>
                        <h4>Nouveau Statut :</h4>
                        <SearchableSelect
                            options={[
                                {label: InvoiceStatus.PAID, value: PAID},
                                {label: InvoiceStatus.PENDING, value: PENDING},
                                {label: InvoiceStatus.CANCELLED, value: CANCELLED},
                            ]}
                            placeholder={'Sélectionner un statut'}
                            onSelect={(option) => setNewStatus(option.value)}
                        />
                    </div>
                }
                footer={
                    <div className={styles.buttons}>
                        <Button title={'Valider'} action={handleEdit} color={'primary'}/>
                        <Button title={'Annuler'} action={() => setEditModalVisible(false)} color={'secondary'}/>
                    </div>
                }
                modalVisible={editModalVisible}
                setModalVisible={setEditModalVisible}
            />
        </div>
    );
}