import {Alert, Linking, Platform, Pressable, StyleSheet, Text, View} from "react-native";
import Icon from "@/components/ui/icon/Icon";
import {CameraType, CameraView, useCameraPermissions} from "expo-camera";
import {useCallback, useState} from "react";
import {useSafeAreaInsets} from "react-native-safe-area-context";
import Ionicons from '@expo/vector-icons/Ionicons';
import TransparentWindow from "@/components/feature/scanner/TransparentWindow";
import * as IntentLauncher from 'expo-intent-launcher';
import {useFocusEffect, useRouter} from "expo-router";
import Button from "@/components/ui/buttons/Button";

export default function Scan() {
    const [isCameraActive, setIsCameraActive] = useState(true);
    const [facing, setFacing] = useState<CameraType>('back');
    const [isFlashOn, setIsFlashOn] = useState(false);
    const [permission, requestPermission] = useCameraPermissions();
    const insets = useSafeAreaInsets();
    const router = useRouter();

    useFocusEffect(useCallback(() => {
        setIsCameraActive(true);
        return () => setIsCameraActive(false);
    }, []));

    const openAppSettings = () => {
        if (Platform.OS === 'ios') {
            Linking.openURL('app-settings:');
        } else if (Platform.OS === 'android') {
            IntentLauncher.startActivityAsync(
                IntentLauncher.ActivityAction.APPLICATION_DETAILS_SETTINGS,
                {data: 'package:' + 'com.trinity.id'}
            );
        }
    };

    if (!permission) {
        return <View/>;
    }

    if (!permission.granted) {
        if (permission.status === 'denied') {
            if (permission.canAskAgain) {
                requestPermission();
            } else {
                Alert.alert(
                    'Aïe ! 🤕',
                    'Vous avez refusé l\'accès à la caméra. Vous pouvez autoriser l\'accès dans les paramètres de votre téléphone.', [
                        {text: 'Annuler', style: 'cancel'},
                        {text: 'Paramètres', onPress: openAppSettings},
                    ]
                );
            }
            return <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
                <Text>Vous nous avez refusé l'accès à la caméra</Text>
                <Text>Retournez dans la page d'accueil ou allez dans vos paramètres</Text>
                <Button title={'Paramètres'} action={openAppSettings} color={'accent'} icon={'Cog'}/>
            </View>;
        }
    }

    const toggleCameraFacing = (): void => {
        setFacing(current => (current === 'back' ? 'front' : 'back'));
    }

    return (
        <View style={{flex: 1}}>
            <CameraView
                active={isCameraActive}
                style={[styles.camera, {paddingTop: insets.top}]}
                enableTorch={isFlashOn}
                facing={facing}
                barcodeScannerSettings={{
                    barcodeTypes: ['ean13'],
                }}
                onBarcodeScanned={({data}) => {
                    setIsCameraActive(false);
                    router.navigate(`products/${data}`);
                }}
            >
                <Pressable
                    onPress={() => setIsFlashOn(!isFlashOn)}
                    style={[styles.icon, {top: insets.top + 32, left: 32}]}
                >
                    {isFlashOn ?
                        <Ionicons name="flash-off" size={32} color="white"/>
                        :
                        <Ionicons name="flash" size={32} color="white"/>
                    }
                </Pressable>
                <Pressable
                    onPress={toggleCameraFacing}
                    style={[styles.icon, {top: insets.top + 32, right: 32}]}
                >
                    <Icon
                        name={'RefreshCw'}
                        color={'white'}
                        size={32}
                    />
                </Pressable>
                <TransparentWindow/>
            </CameraView>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        paddingBottom: 20
    },
    message: {
        textAlign: 'center',
        paddingBottom: 10,
    },
    camera: {
        flex: 1,
    },
    icon: {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        width: 52,
        height: 52,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 100,
        position: 'absolute',
        zIndex: 1000,
    }
});