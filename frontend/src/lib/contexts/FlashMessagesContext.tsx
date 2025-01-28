import React, { createContext, useContext, useState, useCallback } from 'react';
import { FlashMessage } from '@/components/ui/flash-messages/FlashMessage';
import {MessageType} from "@/components/ui/flash-messages/types";

interface FlashContextType {
    showMessage: (type: MessageType, message: string) => void;
}

const FlashContext = createContext<FlashContextType | undefined>(undefined);


export const FlashProvider = ({ children }: { children: React.ReactNode }) => {
    const [currentMessage, setCurrentMessage] = useState<{
        type: MessageType;
        message: string;
    } | null>(null);

    const showMessage = useCallback((type: MessageType, message: string) => {
        setCurrentMessage({ type, message });
    }, []);

    return (
        <FlashContext.Provider value={{ showMessage }}>
            {children}
            {currentMessage && (
                <FlashMessage
                    type={currentMessage.type}
                    message={currentMessage.message}
                    onClose={() => setCurrentMessage(null)}
                />
            )}
        </FlashContext.Provider>
    );
};

export const useFlash = () => {
    const context = useContext(FlashContext);
    if (!context) {
        throw new Error('useFlash must be used within a FlashProvider');
    }
    return context;
};