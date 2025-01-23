import React from 'react';
import styles from './ProductCardSkeleton.module.css';

const ProductCardSkeleton = () => {
    return (
        <div role={'card-skeleton'} className={styles.cardSkeleton}>
            <div className={styles.cardHeader}>
                <div className={styles.titleSkeleton}></div>
                <div className={styles.statusSkeleton}></div>
            </div>
            <div className={styles.cardContent}>
                {[...Array(5)].map((_, index) => (
                    <div key={index} className={styles.contentRow}>
                        <div className={styles.labelSkeleton}></div>
                        <div className={styles.valueSkeleton}></div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProductCardSkeleton;