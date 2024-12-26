import React from 'react';

export default function MainLayout({
                                            children,
                                        }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div>
            <h1>Main</h1>
            {children}
        </div>
    );
}