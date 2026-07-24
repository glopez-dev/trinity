import {StyleSheet} from "react-native";
import {colors} from "@/lib/constants/Colors";

export const styles = StyleSheet.create({
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 12,
        padding: 16,
        borderBottomColor: colors.primary,
        borderBottomWidth: 1,
    },
    headerLeft: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 12,
    },
    profileIcon: {
        backgroundColor: colors.accent,
        borderRadius: 100,
        padding: 12,
        alignItems: 'center',
        justifyContent: 'center',
        aspectRatio: 1,
        width: 64
    },
    profileInfo: {
        marginLeft: 12
    },
    name: {
        fontSize: 16,
        fontWeight: 'bold',
        lineHeight: 36
    },
    email: {
        fontSize: 14,
    },
})