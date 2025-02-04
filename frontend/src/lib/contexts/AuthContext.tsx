'use client';

import { createContext, useContext, ReactNode } from 'react';
import Cookies from "js-cookie";
import {AuthContextType} from "@/lib/types/user/auth";


const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const setToken = (jwt: string) => {
        Cookies.set('auth_token', jwt, {
            expires: 30,
            secure: true,
            sameSite: 'strict'
        });
    };

    const logout = () => {
        Cookies.remove('auth_token');
    };

    const checkAuth = () => {
        const token = Cookies.get('auth_token');
        return !!token;
    };

    const value = {
        setToken,
        logout,
        checkAuth,
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth doit être utilisé avec un AuthProvider');
    }
    return context;
}