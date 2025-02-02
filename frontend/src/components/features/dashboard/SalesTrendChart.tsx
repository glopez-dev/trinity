import React from 'react';
import {CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis} from 'recharts';

const salesTrendData = [
    {month: "Jan", revenue: 45000, profit: 15000},
    {month: "Fév", revenue: 52000, profit: 18000},
    {month: "Mar", revenue: 48000, profit: 16500},
    {month: "Avr", revenue: 60000, profit: 21000},
    {month: "Mai", revenue: 55000, profit: 19000},
    {month: "Jun", revenue: 65000, profit: 22000}
];

const SalesTrendChart = () => {
    return (
        <>
            <h3>Tendance des Ventes et Profits</h3>
            <ResponsiveContainer width="100%" height={280}>
                <LineChart
                    data={salesTrendData}
                    margin={{top: 15, right: 30, left: 20, bottom: 5}}
                >
                    <CartesianGrid strokeDasharray="3 3" className="opacity-50"/>
                    <XAxis
                        dataKey="month"
                        tick={{fill: '#4B5563'}}
                    />
                    <YAxis
                        tick={{fill: '#4B5563'}}
                        tickFormatter={(value) => `${value / 1000}k€`}
                    />
                    <Tooltip
                        formatter={(value) => [`${value.toLocaleString()}€`]}
                        contentStyle={{
                            backgroundColor: 'rgba(255, 255, 255, 0.9)',
                            borderRadius: '6px',
                            border: '1px solid #E5E7EB'
                        }}
                    />
                    <Legend/>
                    <Line
                        type="monotone"
                        dataKey="revenue"
                        stroke="#4A6741"
                        strokeWidth={2}
                        dot={{fill: '#4A6741', strokeWidth: 2}}
                        activeDot={{r: 8}}
                        name="Chiffre d'affaires"
                    />
                    <Line
                        type="monotone"
                        dataKey="profit"
                        stroke="#8CB682"
                        strokeWidth={2}
                        dot={{fill: '#8CB682', strokeWidth: 2}}
                        activeDot={{r: 8}}
                        name="Profit"
                    />
                </LineChart>
            </ResponsiveContainer>
        </>
    );
};

export default SalesTrendChart;