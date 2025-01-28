import React from 'react';
import styles from './DailyMetricCard.module.css';
import Icon from "@/components/ui/icons/Icon";
import {icons} from "lucide-react";

type DailyMetricCardProps = {
    data: {
        title: string,
        value: number,
        variation: number,
        icon: keyof typeof icons
    }
}

function DailyMetricCard({data}: DailyMetricCardProps) {

    return (
        <div className={styles.card}>
            <div className={styles.cardContent}>
                <div className={styles.cardHeader}>
                    <h4 className={styles.cardTitle}>{data.title}</h4>
                    <Icon name={data.icon} size={16} color={'#4A6741'}/>
                </div>
                <h3>{data.value}</h3>
                <p className={`${data.variation > 0 ? styles.cardVariationPos : styles.cardVariationNeg}`}>{data.variation}%</p>
            </div>
        </div>
    );
}

export default DailyMetricCard;