import type { Metadata } from "next";
import "@/styles/globals.css";
import React from "react";

export const metadata: Metadata = {
  title: "Trinity",
  description: "La plateforme de gestion de vos produits, factures et clients",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        {children}
      </body>
    </html>
  );
}
