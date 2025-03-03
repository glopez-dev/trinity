import React from 'react';
import { View } from 'react-native';
import { FlashMessage } from '@/components/ui/flashMessage/FlashMessage';
import { useFlashStore } from './useFlashStore';

export const FlashMessagesProvider = ({ children }: { children: React.ReactNode }) => {
    const { currentMessage, hideMessage } = useFlashStore();

    return (
        <View style={{ flex: 1 }}>
            {children}

            {currentMessage && (
                <FlashMessage
                    type={currentMessage.type}
                    message={currentMessage.message}
                    onClose={hideMessage}
                />
            )}
        </View>
    );
};