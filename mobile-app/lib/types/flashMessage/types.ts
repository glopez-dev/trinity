export type MessageType = 'success' | 'error' | 'warning' | 'info';

export interface FlashMessageProps {
    message: string;
    type: MessageType;
    duration?: number;
    onClose?: () => void;
}