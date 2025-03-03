import { create } from 'zustand';
import { MessageType } from '@/lib/types/flashMessage/types';

interface FlashState {
    currentMessage: {
        type: MessageType;
        message: string;
    } | null;
    showMessage: (type: MessageType, message: string) => void;
    hideMessage: () => void;
}

export const useFlashStore = create<FlashState>((set) => ({
    currentMessage: null,

    showMessage: (type: MessageType, message: string) => {
        set({ currentMessage: { type, message } });
    },

    hideMessage: () => {
        set({ currentMessage: null });
    },
}));

export const useFlashMessage = () => {
    const { showMessage } = useFlashStore();

    return {
        success: (message: string) => showMessage('success', message),
        error: (message: string) => showMessage('error', message),
        warning: (message: string) => showMessage('warning', message),
        info: (message: string) => showMessage('info', message),
    };
};