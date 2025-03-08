import {Text, View} from "react-native";
import Icon from "@/components/ui/icon/Icon";
import {colors} from "@/lib/constants/Colors";
import {styles} from "@/components/feature/profile/header/styles";

export default function ProfileHeader() {
    return (
        <View accessibilityHint={'header'} style={styles.header}>
            <View style={styles.headerLeft}>
                <View accessibilityHint={'icon-profil'} style={styles.profileIcon}>
                    <Icon name={'UserRound'} color={'white'} size={32}/>
                </View>
                <View accessibilityHint={'name-email'} style={styles.profileInfo}>
                    <Text style={styles.name}>John Doe</Text>
                    <Text style={styles.email}>jonh.doe@email.com</Text>
                </View>
            </View>
            <View accessibilityHint={'edit'}>
                <Icon name={'SquarePen'} color={colors.primary} size={20}/>
            </View>
        </View>
    )
}
