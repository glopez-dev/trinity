import React from 'react';
import {Cell, Legend, Pie, PieChart, ResponsiveContainer, Tooltip} from 'recharts';
import styles from './DailyMetrics.module.css';

interface CategoryData {
    name: string;
    value: number;
    revenue: number;
}

interface CustomTooltipProps {
    active?: boolean;
    payload?: Array<{
        payload: CategoryData;
    }>;
}

const salesByCategoryData = {
    "Céréales": {
        percentage: 15,
        revenue: 9750
    },
    "Boissons végétales": {
        percentage: 11,
        revenue: 7150
    },
    "Confiserie": {
        percentage: 19,
        revenue: 12350
    },
    "Produits laitiers": {
        percentage: 12,
        revenue: 7800
    },
    "Huiles": {
        percentage: 8,
        revenue: 5200
    },
    "Fruits": {
        percentage: 14,
        revenue: 9100
    },
    "Boulangerie": {
        percentage: 9,
        revenue: 5850
    },
    "Légumes": {
        percentage: 12,
        revenue: 7800
    }
};

// Palette de couleurs harmonieuse basée sur notre charte graphique
const COLORS = ['#4A6741', '#6B8E61', '#8CB682', '#ADC2A4', '#CEDFCA', '#90A98E', '#718E6F', '#536B50'];

const SalesCategoryChart = () => {
    const formattedData: CategoryData[] = Object.entries(salesByCategoryData).map(([name, info]) => ({
        name,
        value: info.percentage,
        revenue: info.revenue,
    }));

    const CustomTooltip: React.FC<CustomTooltipProps> = ({active, payload}) => {
        if (active && payload && payload.length) {
            const data = payload[0].payload;
            return (
                <div className={styles.customTooltipContainer}>
                    <p>{data.name}</p>
                    <p>Part: {data.value}%</p>
                    <p>CA: {data.revenue.toLocaleString()}€</p>
                </div>
            );
        }
        return null;
    };

    return (
        <>
            <h3>Répartition des Ventes par Catégorie</h3>
            <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                    <Pie
                        data={formattedData}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        outerRadius={100}
                        fill="#8884d8"
                        dataKey="value"
                        label={({name, percent}) => `${name} ${(percent * 100).toFixed(0)}%`}
                    >
                        {formattedData.map((entry, index) => (
                            <Cell
                                key={`cell-${index}`}
                                fill={COLORS[index % COLORS.length]}
                                style={{outline: 'none'}}
                            />
                        ))}
                    </Pie>
                    <Tooltip content={<CustomTooltip/>}/>
                    <Legend
                        layout="horizontal"
                        verticalAlign="bottom"
                        align="center"
                        wrapperStyle={{
                            paddingTop: '20px'
                        }}
                    />
                </PieChart>
            </ResponsiveContainer>
        </>
    );
};

export default SalesCategoryChart;