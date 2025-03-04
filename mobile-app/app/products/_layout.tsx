import {Stack, useRouter} from 'expo-router';
import {colors} from "@/lib/constants/Colors";
import Icon from "@/components/ui/icon/Icon";

export default function ProductLayout() {
    const router = useRouter();
    return (
        <Stack>
            <Stack.Screen name="[barcode]" options={{
                title: 'Détails du produit',
                headerStyle: {
                    backgroundColor: colors.primary,
                },
                headerTintColor: colors.secondary,
                contentStyle: {
                    backgroundColor: colors.secondary,
                },
                headerLeft: () => <Icon color={colors.secondary} name={'ArrowLeft'} onPress={() => router.canGoBack() ? router.back() : router.push('/')}/>,
            }}/>
        </Stack>
    );
}