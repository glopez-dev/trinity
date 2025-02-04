import {AuthProvider} from "@/lib/contexts/AuthContext";
import {FlashProvider} from "@/lib/contexts/FlashMessagesContext";
import {render} from "@testing-library/react";
import React from "react";

const AllProviders = ({children}: { children: React.ReactNode }) => {
    return (
        <AuthProvider>
            <FlashProvider>
                {children}
            </FlashProvider>
        </AuthProvider>
    )
}

const AuthProviderOnly = ({children}: { children: React.ReactNode }) => {
    return (
        <AuthProvider>
            {children}
        </AuthProvider>
    )
}

const FlashProviderOnly = ({children}: { children: React.ReactNode }) => {
    return (
        <FlashProvider>
            {children}
        </FlashProvider>
    )
}

export function renderWithProviders(ui: React.ReactElement, provider: 'auth' | 'flash' | 'all' = 'all') {
    if (provider === 'auth') {
        return render(ui, {wrapper: AuthProviderOnly})
    } else if (provider === 'flash') {
        return render(ui, {wrapper: FlashProviderOnly})
    }
    return render(ui, {wrapper: AllProviders})
}