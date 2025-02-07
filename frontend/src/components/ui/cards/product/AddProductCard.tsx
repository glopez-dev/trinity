import {ProductOFF} from "@/lib/types/product/product";
import styles from './addProductCard.module.css';
import Image from "next/image";

interface ProductCardProps {
    product: ProductOFF;
    isSelected: boolean;
    onSelect: (product: ProductOFF) => void;
    onMouseEnter: (product: ProductOFF) => void;
    onMouseLeave: () => void;
    tooltip?: string | null;
}

export default function AddProductCard({
        product,
        isSelected,
        onSelect,
        onMouseEnter,
        onMouseLeave,
        tooltip
}: Readonly<ProductCardProps>) {
    return (
        <button
            data-testid="product-card"
            className={styles.productCard + (isSelected ? ' ' + styles.selectedCard : '')}
            onClick={() => onSelect(product)}
            onMouseEnter={() => onMouseEnter(product)}
            onMouseLeave={() => onMouseLeave()}
        >
            <div className={styles.imageWrapper}>
                {product.selectedImages ?
                <Image
                    src={product.selectedImages.thumb.fr || product.selectedImages.thumb.en}
                    alt={product.brand}
                    className={styles.productImage}
                    width={100}
                    height={100}
                />
                    :
                    <div className={styles.productImage} style={{backgroundColor: 'lightgrey'}} />
                }
            </div>
            <div className={styles.productContent}>
                <div className={styles.brandName}>{product.brand}</div>
                <div className={styles.productName}>{product.name}</div>
            </div>
            <div className={styles.tooltipContent}>
                {product.ingredients ?? tooltip}
            </div>
        </button>
    );
}