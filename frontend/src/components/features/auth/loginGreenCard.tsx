import React from 'react';
import styles from './loginGreenCard.module.css';
import GreenCardItem, {GreenCardItemProps} from "@/components/features/auth/GreenCardItem";

const GreenCardItems: GreenCardItemProps[] = [
    {
        title: 'Gérez vos produits et votre stocks',
        image: {
            src: 'step-1',
            width: 266,
            height: 210
        },
        isReverse: false
    },
    {
        title: 'Visualisez vos performances',
        image: {
            src: 'step-2',
            width: 300,
            height: 210
        },
        isReverse: true
    },
    {
        title: 'Suivez vos transactions',
        image: {
            src: 'step-3',
            width: 251,
            height: 210
        },
        isReverse: false
    },
]

const LoginGreenCard: React.FC = () => {
    return (
        <div className={styles.card}>
            {GreenCardItems.map((item, index) => (
                <GreenCardItem key={index} title={item.title} image={item.image} isReverse={item.isReverse}/>
            ))}
        </div>
    );
}


export default LoginGreenCard;