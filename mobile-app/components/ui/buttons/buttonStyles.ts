import { StyleSheet } from 'react-native';
import {colors} from '@/lib/constants/Colors';

const styles = StyleSheet.create({
    wrapper: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 10,
        paddingVertical: 12,
        paddingHorizontal: 16,
        height: 40,
        minWidth: 100,
        borderRadius: 4,
        alignSelf: 'center',

    },
    text: {
        fontWeight: '500',
        fontSize: 14,
    },
    primary: {
        backgroundColor: colors.primary,
    },
    primaryText: {
        color: colors.secondary,
    },
    secondary: {
        backgroundColor: colors.secondary,
    },
    secondaryText: {
        color: colors.primary,
    },
    accent: {
        backgroundColor: colors.accent,
    },
    accentText: {
        color: colors.secondary,
    },
    disabled: {
        opacity: 0.7,
    },
    full: {
        width: '80%',
    },
});

export default styles;