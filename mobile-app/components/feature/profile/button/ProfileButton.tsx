import Icon from "@/components/ui/icon/Icon";
import {ProfileButtonProps} from "@/components/feature/profile/button/types";
import {colors} from "@/lib/constants/Colors";
import {Text, TouchableOpacity, View} from "react-native";
import {useRouter} from "expo-router";
import styles from './styles'


export default function ProfileButton({title, link, icon}: Readonly<ProfileButtonProps>) {
    const router = useRouter();

    return (
        <View style={styles.outerShadow}>
            <View style={styles.innerShadow}>
                <TouchableOpacity accessibilityHint={'profile-button'} style={styles.btn} onPress={() => router.push(link)}>
                    <View style={styles.btnLeft}>
                        <Icon name={icon} color={colors.primary} size={20}/>
                        <Text>{title}</Text>
                    </View>
                    <View>
                        <Icon name={'ChevronRight'} color={colors.grey_1} size={20}/>
                    </View>
                </TouchableOpacity>
            </View>
        </View>
    )
}