'use client'

import React from 'react';
import DailyMetrics from "@/components/features/dashboard/DailyMetrics";
import StockLevelsChart from "@/components/features/dashboard/StockLevelsChart";
import SalesCategoryChart from "@/components/features/dashboard/SalesCategoryChart";
import styles from '@/styles/dashboard/page.module.css';
import SalesTrendChart from "@/components/features/dashboard/SalesTrendChart";
import DayRangePicker from "@/components/ui/buttons/day-picker/DayRangePicker";
import IconButton from "@/components/ui/buttons/icon-button/IconButton";

export default function Dashboard() {
    return (
        <div>
            <h3>Dashboard</h3>
            <div className={styles.headerContainer}>
                <DayRangePicker />
                <IconButton icon={'RefreshCw'} onClick={() => console.log('Refresh data')} />
            </div>
            <DailyMetrics/>
            <div className={styles.topGraphContainer}>
                <div className={styles.graphicCard}>
                    <StockLevelsChart/>
                </div>
                <div className={styles.graphicCard}>
                    <SalesTrendChart/>
                </div>
            </div>
            <div className={styles.bottomGraphContainer}>
                <div className={styles.graphicCard}>
                    <SalesCategoryChart/>
                </div>
            </div>
        </div>
    );
}