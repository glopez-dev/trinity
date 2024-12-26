"use client";
import React from 'react';
import Navigation from "@/components/layout/navigation";
import styles from '@/styles/MainLayout.module.css';

export default function MainLayout({
                                            children,
                                        }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className={styles.mainContainer}>
            <Navigation />
            <div className={styles.container}>
                {children}
            </div>
        </div>
    );
}