import {SafeAreaView, StyleSheet, View} from "react-native";
import Button from "@/components/ui/buttons/Button";
import {colors} from "@/lib/constants/Colors";
import ProfileButton from "@/components/feature/profile/button/ProfileButton";
import ProfileHeader from "@/components/feature/profile/header/ProfileHeader";
import {useAuthStore} from "@/lib/stores/auth/useAuthStore";
import {useRouter} from "expo-router";

export default function Profile() {
    const {logout} = useAuthStore();
    const router = useRouter();
    const handleLogout = async () => {
        await logout();
        router.push('/login')
    }
    return (
        <SafeAreaView style={[{
            backgroundColor: colors.secondary,
            flex: 1,
        }]}>
            <View style={styles.container}>
                <ProfileHeader name={'John Doe'} email={'john.doe@gmail.com'}/>
                <View accessibilityHint={'body'} style={styles.btnContainer}>
                    <ProfileButton title={'Mes commandes'} link={'/history'} icon={'ShoppingBag'}/>
                    <ProfileButton title={'Informations personnelles'} link={'/profile/informations'} icon={'UserRoundCog'}/>
                    <ProfileButton title={'Adresse de livraison'} link={'/profile/address'} icon={'Truck'}/>
                    <ProfileButton title={'Notifications'} link={'/profile/notification'} icon={'BellRing'}/>
                </View>
                <View accessibilityHint={'footer'} style={styles.footer}>
                    <Button title={'Déconnexion'} action={handleLogout} size={'full'}/>
                </View>
            </View>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingHorizontal: 12,
        paddingVertical: 24,
    },
    btnContainer: {
        marginTop: 24,
        gap: 24
    },
    footer: {
        marginTop: 'auto',
    },

});