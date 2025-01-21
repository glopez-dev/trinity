import styles from "./ProductCard.module.css";
import {getStockStatus, STOCK_STATUS} from "@/lib/constants/products";
import Badge, {BadgeProps} from "@/components/ui/badge/Badge";
import {Product} from "@/lib/types/product/product";

interface ProductCardProps {
    key: string;
    product: Product;
}

export const ProductCard = ({product}: ProductCardProps) => {
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

    return (
        <div className={styles.card} role={'product-card'}>
            <div className={styles.cardHeader}>
                <p className={styles.cardHeaderName}>{product.name}</p>
                <Badge text={badgeData.text} type={badgeData.type}/>
            </div>
            <div className={styles.cardBody}>
                <div className={styles.cardItem}>
                    <p className={styles.cardItemName}>Prix</p>
                    <p className={styles.cardItemValue}>{product.price} €</p>
                </div>
                <div className={styles.cardItem}>
                    <p className={styles.cardItemName}>Quantité</p>
                    <p className={styles.cardItemValue}>{product.stock.currentQuantity}</p>
                </div>
                <div className={styles.cardItem}>
                    <p className={styles.cardItemName}>Seuil Minimal</p>
                    <p className={styles.cardItemValue}>{product.stock.minThreshold}</p>
                </div>
                <div className={styles.cardItem}>
                    <p className={styles.cardItemName}>Catégorie</p>
                    <p className={styles.cardItemValue}>{product.category}</p>
                </div>
                <div className={styles.cardItem}>
                    <p className={styles.cardItemName}>Dernière mise à jour</p>
                    <p className={styles.cardItemValue}>{product.lastUpdate.toLocaleDateString()}</p>
                </div>
            </div>
        </div>
    );
}
