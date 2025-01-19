import React, {FC} from 'react';
import styles from './loginGreenCard.module.css';
import Image from "next/image";

export interface GreenCardItemProps {
    title: string;
    image: {
        src: string;
        width: number;
        height: number;
    };
    isReverse: boolean;
}

const GreenCardItem: FC<GreenCardItemProps> = ({title, image, isReverse}) => {
    return (
        <div className={`${styles.cardItem} ${isReverse && styles.cardItemReverse}`}>
            <h2 className={styles.cardItemTitle}>{title}</h2>
            <Image
                src={`images/auth/${image.src}.svg`}
                alt={`image ${image.src}`}
                className={styles.cardItemImage}
                width={image.width}
                height={image.height}
                priority={false}
            />
        </div>
    );
}

export default GreenCardItem;