import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, Animated, Dimensions } from 'react-native';
import { AlertTriangle, CheckCircle, Info, X, XCircle } from 'lucide-react-native';
import { MessageType } from '@/lib/types/flashMessage/types';
import {
    styles,
    getBackgroundColor,
    getBorderColor,
    getTextColor,
    getProgressColor,
    getIconColor
} from './flashMessageStyles';
import {useSafeAreaInsets} from "react-native-safe-area-context";

interface FlashMessageProps {
    message: string;
    type: MessageType;
    duration?: number;
    onClose?: () => void;
}

const getIcon = (type: MessageType) => {
    const color = getIconColor(type);
    switch (type) {
        case 'success':
            return <CheckCircle size={20} color={color} />;
        case 'error':
            return <XCircle size={20} color={color} />;
        case 'warning':
            return <AlertTriangle size={20} color={color} />;
        case 'info':
            return <Info size={20} color={color} />;
    }
};

export const FlashMessage = ({
                                 message,
                                 type,
                                 duration = 5000,
                                 onClose
                             }: FlashMessageProps) => {
    const [visible, setVisible] = useState(true);
    const slideAnimation = useState(new Animated.Value(-100))[0];
    const progressAnimation = useState(new Animated.Value(0))[0];
    const insets = useSafeAreaInsets();

    useEffect(() => {
        Animated.timing(slideAnimation, {
            toValue: insets.top + 16,
            duration: 300,
            useNativeDriver: true,
        }).start();

        if (duration) {
            Animated.timing(progressAnimation, {
                toValue: Dimensions.get('window').width - 32,
                duration,
                useNativeDriver: false,
            }).start();

            const timer = setTimeout(() => {
                handleClose();
            }, duration);

            return () => clearTimeout(timer);
        }
    }, [duration]);

    const handleClose = () => {
        Animated.timing(slideAnimation, {
            toValue: -100,
            duration: 300,
            useNativeDriver: true,
        }).start(() => {
            setVisible(false);
            onClose?.();
        });
    };

    if (!visible) return null;

    return (
        <Animated.View
            style={[
                styles.container,
                {
                    transform: [{ translateY: slideAnimation }],
                    backgroundColor: getBackgroundColor(type),
                    borderColor: getBorderColor(type)
                }
            ]}
            accessibilityRole="alert"
            testID={`flash-message-${type}`}
        >
            <View style={styles.content}>
                <View style={styles.icon}>{getIcon(type)}</View>
                <Text style={[styles.text, { color: getTextColor(type) }]}>{message}</Text>
            </View>

            <TouchableOpacity
                style={styles.closeButton}
                onPress={handleClose}
                accessibilityRole="button"
                accessibilityLabel="Close message"
            >
                <X size={20} color={getTextColor(type)} />
            </TouchableOpacity>

            <View style={styles.progressBarContainer}>
                <Animated.View
                    style={[
                        styles.progressBar,
                        {
                            width: progressAnimation,
                            backgroundColor: getProgressColor(type)
                        }
                    ]}
                />
            </View>
        </Animated.View>
    );
};