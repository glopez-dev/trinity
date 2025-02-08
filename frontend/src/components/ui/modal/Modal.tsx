import React from 'react';
import styles from './Modal.module.css';
import Icon from "@/components/ui/icons/Icon";

interface ModalProps {
    title: string;
    body: React.ReactNode;
    footer: React.ReactNode;
    modalVisible: boolean;
    setModalVisible: React.Dispatch<React.SetStateAction<boolean>>;
}

function Modal({title, body, footer, modalVisible, setModalVisible}: Readonly<ModalProps>) {
    if (!modalVisible) return null;

    return (
        <div className={styles.container}>
            <div className={styles.modal}>
                <div className={styles.header}>
                    <h2>{title}</h2>
                    <button className={styles.close} onClick={() => setModalVisible(false)}>
                        <Icon name={'X'} />
                    </button>
                </div>
                <div className={styles.body}>
                    {body}
                </div>
                <div className={styles.footer}>
                    {footer}
                </div>
            </div>
        </div>
    );
}

export default Modal;