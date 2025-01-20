import React from "react";
import styles from "@/styles/auth/AuthLayout.module.css";

export default function AuthLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className={styles.container}>
            {children}
        </div>
    );
}