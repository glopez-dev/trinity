import React, {useEffect, useState} from 'react';
import {AlertTriangle, CheckCircle, Info, X, XCircle} from 'lucide-react';
import styles from './styles.module.css';
import {FlashMessageProps, MessageType} from "./types";

const getIcon = (type: MessageType) => {
    switch (type) {
        case 'success':
            return <CheckCircle size={20}/>;
        case 'error':
            return <XCircle size={20}/>;
        case 'warning':
            return <AlertTriangle size={20}/>;
        case 'info':
            return <Info size={20}/>;
    }
};

export const FlashMessage = ({
                                 message,
                                 type,
                                 duration = 5000,
                                 onClose
                             }: FlashMessageProps) => {
    const [progress, setProgress] = useState(100);
    const [visible, setVisible] = useState(true);

    useEffect(() => {
        if (duration) {
            setProgress(0);
            const startTime = Date.now();
            const updateProgress = () => {
                const elapsed = Date.now() - startTime;
                const newProgress = (elapsed / duration) * 100;

                if (newProgress < 100) {
                    setProgress(newProgress);
                    requestAnimationFrame(updateProgress);
                }
            };

            requestAnimationFrame(updateProgress);

            const timer = setTimeout(() => {
                setVisible(false);
                onClose?.();
            }, duration);

            return () => clearTimeout(timer);
        }
    }, [duration, onClose]);

    if (!visible) return null;

    return (
        <div role={'alert'} className={`${styles.container} ${styles[type]}`}>
            <div className={styles.content}>
                <span className={styles.icon}>{getIcon(type)}</span>
                <span className={styles.text}>{message}</span>
            </div>
            <button
                role={'close-button'}
                className={styles.closeButton}
                onClick={() => {
                    setVisible(false);
                    onClose?.();
                }}
            >
                <X size={20}/>
            </button>
            <div
                className={styles.progressBar}
                style={{
                    width: `${progress}%`,
                    backgroundColor: type === 'success' ? 'rgb(5, 150, 105)' :
                        type === 'error' ? 'rgb(220, 38, 38)' :
                            type === 'warning' ? 'rgb(217, 119, 6)' :
                                'rgb(59, 130, 246)'
                }}
            />
        </div>
    );
};