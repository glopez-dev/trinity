import {Stack} from 'expo-router';

export default function ModalLayout() {
    return (
        <Stack>
            <Stack.Screen name="notifications" options={{
                presentation: 'modal',
            }}/>
        </Stack>
    );
}