import React, { FC } from 'react';
import { TouchableOpacity, Text, View } from 'react-native';
import Icon from '@/components/ui/icon/Icon';
import { ButtonProps } from "./types";
import styles from './buttonStyles';

const Button: FC<ButtonProps> = ({
                                     title,
                                     color = 'primary',
                                     action,
                                     size = '',
                                     icon = null,
                                     disabled = false,
                                     type = 'button'
                                 }) => {
    let iconColor = '';
    if (color === 'secondary') {
        iconColor = '#4A6741';
    } else {
        iconColor = '#F5F1E8';
    }

    const buttonStyle = [
        styles.wrapper,
        styles[color],
        size === 'full' && styles.full,
        disabled && styles.disabled
    ];

    const textStyle = [
        styles.text,
        color === 'primary' && styles.primaryText,
        color === 'secondary' && styles.secondaryText,
        color === 'accent' && styles.accentText,
    ];

    return (
        <View>
            <TouchableOpacity
                style={buttonStyle}
                onPress={action}
                disabled={disabled}
                accessibilityRole="button"
            >
                <Text style={textStyle}>{title}</Text>
                {icon && <Icon name={icon} size={14} color={iconColor} testID={`icon-${icon}`} />}
            </TouchableOpacity>
        </View>
    );
};

export default Button;