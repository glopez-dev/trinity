import React from 'react';
import { useAuthStore } from './useAuthStore';

interface AuthProviderProps {
    children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const initialize = useAuthStore(state => state.initialize);

    initialize();

    return <>{children}</>;
};