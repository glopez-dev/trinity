import {StyleSheet} from "react-native";

const styles = StyleSheet.create({
    btn: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 10,
        borderRadius: 4,
        backgroundColor: 'white',
    },
    outerShadow: {
        backgroundColor: 'transparent',
        borderRadius: 4,
        shadowColor: '#000000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 4,
    },
    innerShadow: {
        backgroundColor: 'transparent',
        borderRadius: 4,
        shadowColor: '#000000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.1,
        shadowRadius: 6,
        elevation: 6,
    },
    btnLeft: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 10,
    },
});

export default styles;
