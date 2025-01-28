'use client'

import "react-day-picker/style.css";
import "@/styles/globals.css";
import React from "react";
import {FlashProvider} from "@/lib/contexts/FlashMessagesContext";


export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
        <head>
            <title>Trinity</title>
            <meta name="description" content="La plateforme de gestion de vos produits, factures et clients"/>
            <link rel="icon" href="images/web-icon-trinity.svg"/>
        </head>
        <body>
        <FlashProvider>
            {children}
        </FlashProvider>
        </body>
        </html>
    );
}
