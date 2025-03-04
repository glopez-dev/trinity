import React from 'react';
import {Dimensions, StyleSheet, View} from 'react-native';

const {width, height} = Dimensions.get('window');
const scanAreaSize = {width: width * 0.7, height: 140};
const scanAreaTop = (height - scanAreaSize.height) / 2;
const scanAreaLeft = (width - scanAreaSize.width) / 2;

export default function TransparentWindow() {
    return (
        <View style={styles.container}>
            <View style={styles.overlay}>
                <View style={[styles.overlaySection, {height: scanAreaTop}]}/>

                <View style={{flexDirection: 'row', height: scanAreaSize.height}}>
                    <View style={[styles.overlaySection, {width: scanAreaLeft}]}/>
                    <View style={styles.transparentArea}>
                        <View style={[styles.corner, styles.topLeftCorner]}/>
                        <View style={[styles.corner, styles.topRightCorner]}/>
                        <View style={[styles.corner, styles.bottomLeftCorner]}/>
                        <View style={[styles.corner, styles.bottomRightCorner]}/>
                    </View>
                    <View style={[styles.overlaySection, {width: scanAreaLeft}]}/>
                </View>

                <View style={[styles.overlaySection, {height: scanAreaTop}]}/>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        zIndex: 1
    },
    overlay: {
        flex: 1,
    },
    overlaySection: {
        backgroundColor: 'rgba(0, 0, 0, 0.6)',
        justifyContent: 'center',
        alignItems: 'center',
    },
    transparentArea: {
        width: scanAreaSize.width,
        height: scanAreaSize.height,
        borderWidth: 1,
        borderColor: 'rgba(255, 255, 255, 0.3)',
        position: 'relative',
    },
    corner: {
        position: 'absolute',
        width: 40,
        height: 40,
        borderColor: '#fff',
    },
    topLeftCorner: {
        top: 0,
        left: 0,
        borderLeftWidth: 2,
        borderTopWidth: 2,
    },
    topRightCorner: {
        top: 0,
        right: 0,
        borderRightWidth: 2,
        borderTopWidth: 2,
    },
    bottomLeftCorner: {
        bottom: 0,
        left: 0,
        borderLeftWidth: 2,
        borderBottomWidth: 2,
    },
    bottomRightCorner: {
        bottom: 0,
        right: 0,
        borderRightWidth: 2,
        borderBottomWidth: 2,
    },
});