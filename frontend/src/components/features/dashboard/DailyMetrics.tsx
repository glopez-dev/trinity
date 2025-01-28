import React from 'react';
import DailyMetricCard from "@/components/ui/cards/dashboard/DailyMetricCard";
import {icons} from "lucide-react";
import styles from './DailyMetrics.module.css';

type DailyMetric = {
    title: string,
    value: number,
    variation: number,
    icon: keyof typeof icons
}

const dailyMetrics: DailyMetric[] = [
    {
        title: "Revenue total",
        value: 12000,
        variation: 12,
        icon: "Euro"
    },
    {
        title: "Commandes",
        value: 156,
        variation: -12,
        icon: "ShoppingCart"
    },
    {
        title: "Produits Vendu",
        value: 1245,
        variation: 8,
        icon: "Package"
    },
    {
        title: "Clients Actifs",
        value: 892,
        variation: 5,
        icon: "Users"
    }
]

function DailyMetrics() {
    return (
        <div className={styles.dailyMetrics}>
            {dailyMetrics.map((metric, index) => (
                <DailyMetricCard key={index} data={metric}/>
            ))}
        </div>
    );
}

export default DailyMetrics;