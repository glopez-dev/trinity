import {SplashScreen, Stack} from 'expo-router';
import {useColorScheme} from 'react-native';
import {FlashMessagesProvider} from '@/lib/stores/flashMessage/FlashMessageProvider';
import {SafeAreaProvider} from "react-native-safe-area-context";
import {useFonts} from "expo-font";
import {useEffect} from "react";


SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
    const colorScheme = useColorScheme();
    const [loaded, error] = useFonts({
        'DM Sans': require('@/assets/fonts/DMSans-VariableFont.ttf')
    })

    useEffect(() => {
        if (loaded || error) {
            SplashScreen.hideAsync();
        }
    }, [loaded, error]);

    if (!loaded && !error) {
        return null;
    }

    return (
        <SafeAreaProvider>
            <FlashMessagesProvider>
                <Stack
                    screenOptions={{
                        headerShown: false,
                        contentStyle: {backgroundColor: colorScheme === 'dark' ? '#000' : '#fff'},
                    }}
                >
                    <Stack.Screen name="(tabs)" options={{headerShown: false}}/>
                    <Stack.Screen name="(auth)" options={{headerShown: false}}/>
                    <Stack.Screen name="(modals)" options={{headerShown: false}}/>
                    <Stack.Screen name="products" options={{headerShown: false}}/>
                </Stack>
            </FlashMessagesProvider>
        </SafeAreaProvider>
    );
}