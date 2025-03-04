import { StyleSheet } from 'react-native';
import { MessageType } from '@/lib/types/flashMessage/types';

export const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        top: 0,
        right: 16,
        left: 16,
        flexDirection: 'row',
        alignItems: 'flex-start',
        justifyContent: 'space-between',
        padding: 16,
        borderRadius: 6,
        borderWidth: 1,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 3,
        elevation: 3,
        overflow: 'hidden',
        zIndex: 1000,
    },
    content: {
        flex: 1,
        flexDirection: 'row',
        alignItems: 'flex-start',
        gap: 12,
    },
    icon: {
        marginTop: 2,
    },
    text: {
        flex: 1,
        fontSize: 14,
        lineHeight: 20,
    },
    closeButton: {
        padding: 2,
        opacity: 0.7,
    },
    progressBarContainer: {
        position: 'absolute',
        bottom: 0,
        left: 0,
        right: 0,
        height: 3,
        backgroundColor: 'rgba(255, 255, 255, 0.4)',
    },
    progressBar: {
        height: 3,
    }
});

export const getBackgroundColor = (type: MessageType): string => {
    switch (type) {
        case 'success':
            return '#ECFDF3';
        case 'error':
            return '#FEF2F2';
        case 'warning':
            return '#FFFBEB';
        case 'info':
            return '#EFF6FF';
    }
};

export const getBorderColor = (type: MessageType): string => {
    switch (type) {
        case 'success':
            return '#A7F3D0';
        case 'error':
            return '#FECACA';
        case 'warning':
            return '#FEF3C7';
        case 'info':
            return '#BFDBFE';
    }
};

export const getTextColor = (type: MessageType): string => {
    switch (type) {
        case 'success':
            return '#065F46';
        case 'error':
            return '#991B1B';
        case 'warning':
            return '#92400E';
        case 'info':
            return '#1E40AF';
    }
};

export const getProgressColor = (type: MessageType): string => {
    switch (type) {
        case 'success':
            return '#059669';
        case 'error':
            return '#DC2626';
        case 'warning':
            return '#D97706';
        case 'info':
            return '#3B82F6';
    }
};

export const getIconColor = (type: MessageType): string => {
    switch (type) {
        case 'success':
            return '#059669';
        case 'error':
            return '#DC2626';
        case 'warning':
            return '#D97706';
        case 'info':
            return '#3B82F6';
    }
};