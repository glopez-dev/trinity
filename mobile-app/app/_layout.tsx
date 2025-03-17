import {Stack} from 'expo-router';
import {useColorScheme} from 'react-native';
import {FlashMessagesProvider} from '@/lib/stores/flashMessage/FlashMessageProvider';
import {SafeAreaProvider} from "react-native-safe-area-context";
import Headers from "@/components/ui/header/Header";

export default function RootLayout() {
    const colorScheme = useColorScheme();

    return (
        <SafeAreaProvider>
            <FlashMessagesProvider>
                <Headers />
                <Stack
                    screenOptions={{
                        headerShown: false,
                        contentStyle: {backgroundColor: colorScheme === 'dark' ? '#000' : '#fff'},
                    }}
                >
                    <Stack.Screen name="(tabs)" options={{headerShown: false}}/>
                    <Stack.Screen name="(auth)" options={{headerShown: false}}/>
                </Stack>
            </FlashMessagesProvider>
        </SafeAreaProvider>
    );
}