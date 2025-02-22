import {Stack} from 'expo-router';
import {SafeAreaView, useColorScheme} from 'react-native';

export default function RootLayout() {
    const colorScheme = useColorScheme();

    return (
        <SafeAreaView style={{flex: 1}}>
            <Stack
                screenOptions={{
                    headerShown: false,
                    contentStyle: {backgroundColor: colorScheme === 'dark' ? '#000' : '#fff'},
                }}
            >
                <Stack.Screen name="(tabs)" options={{headerShown: false}}/>
            </Stack>
        </SafeAreaView>
    );
}