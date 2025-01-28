import React from 'react';
import {Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, TooltipProps, XAxis, YAxis} from 'recharts';
import {getStockStatus} from "@/lib/constants/products";
import styles from './DailyMetrics.module.css';

interface StockData {
    currentStock: number;
    minThreshold: number;
    fullName: string;
}

interface FormattedStockData {
    name: string;
    stock: number;
    threshold: number;
    fullName: string;
}

interface CustomTooltipProps extends TooltipProps<number, string> {
    active?: boolean;
    payload?: Array<{
        value: number;
        name: string;
        payload: FormattedStockData;
    }>;
}


const stockLevelsData: Record<string, StockData> = {
    "P1": {
        currentStock: 35,
        minThreshold: 20,
        fullName: "Bio Organic Quinoa"
    },
    "P2": {
        currentStock: 25,
        minThreshold: 15,
        fullName: "Lait d'Amande Bio"
    },
    "P3": {
        currentStock: 95,
        minThreshold: 30,
        fullName: "Chocolat Noir 70%"
    },
    "P4": {
        currentStock: 35,
        minThreshold: 25,
        fullName: "Yaourt Nature Bio"
    },
    "P5": {
        currentStock: 5,
        minThreshold: 10,
        fullName: "Huile d'Olive"
    }
};

const StockLevelsChart = () => {
    const formattedData: FormattedStockData[] = Object.entries(stockLevelsData).map(([id, info]) => ({
        name: 'Stock ' + getStockStatus(info.currentStock, info.minThreshold) + ' (' + id + ')',
        stock: info.currentStock,
        threshold: info.minThreshold,
        fullName: info.fullName
    }));

    const CustomTooltip = ({active, payload}: CustomTooltipProps) => {
        if (active && payload && payload.length) {
            const data = payload[0].payload;
            return (
                <div className={styles.customTooltipContainer}>
                    <p>{data.fullName}</p>
                    <p>Stock actuel: {payload[0].value}</p>
                    <p>Seuil minimum: {payload[1].value}</p>
                </div>
            );
        }
        return null;
    };

    return (
        <>
            <h3>Niveaux des stocks</h3>
            <ResponsiveContainer width="100%" height={280}>
                <BarChart
                    data={formattedData}
                    margin={{top: 15, right: 30, left: 20, bottom: 5}}
                >
                    <CartesianGrid strokeDasharray="3 3" opacity={0.5}/>
                    <XAxis
                        dataKey="name"
                        tick={{fill: '#4B5563', fontSize: 12}}
                        height={30}
                    />
                    <YAxis
                        tick={{fill: '#4B5563'}}
                        domain={[0, 100]}
                    />
                    <Tooltip content={<CustomTooltip/>}/>
                    <Legend />
                    <Bar
                        dataKey="stock"
                        fill="#C7522A"
                        name="Stock actuel"
                        radius={[4, 4, 0, 0]}
                    />
                    <Bar
                        dataKey="threshold"
                        fill="#4A6741"
                        name="Seuil minimum"
                        radius={[4, 4, 0, 0]}
                    />
                </BarChart>
            </ResponsiveContainer>
        </>
    );
};

export default StockLevelsChart;