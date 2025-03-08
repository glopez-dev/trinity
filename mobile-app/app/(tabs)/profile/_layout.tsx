import {Stack, useRouter} from 'expo-router';
import {colors} from "@/lib/constants/Colors";
import Icon from "@/components/ui/icon/Icon";



export default function ProfileLayout() {
    const router = useRouter();
    const headerOptions = {
        headerStyle: {
            backgroundColor: colors.primary,
        },
        headerTintColor: colors.secondary,
        contentStyle: {
            backgroundColor: colors.secondary,
        },
        headerLeft: () => <Icon color={colors.secondary} name={'ArrowLeft'} onPress={() => router.canGoBack() ? router.back() : router.push('/profile')}/>,
    }
    return (
        <Stack>
            <Stack.Screen name={'index'} options={{
                headerShown: false
            }}/>
            <Stack.Screen name={'informations'} options={{
                title: 'Informations Personnelles',
                ...headerOptions,
            }}/>
            <Stack.Screen name={'address'} options={{
                title: 'Adresse de livraison',
                ...headerOptions,
            }}/>
            <Stack.Screen name={'notification'} options={{
                title: 'Notifications',
                ...headerOptions,
            }}/>
        </Stack>
    );
}