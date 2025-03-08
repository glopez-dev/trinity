import {
    Keyboard,
    KeyboardAvoidingView,
    Platform,
    SafeAreaView,
    StyleSheet,
    Text,
    TouchableWithoutFeedback,
    View
} from "react-native";
import {useState} from "react";
import {api} from "@/lib/API/api";
import {Link, router} from "expo-router";
import Button from "@/components/ui/buttons/Button";
import Input from "@/components/ui/input/Input";
import {FlashMessage} from "@/components/ui/flashMessage/FlashMessage";
import {colors} from "@/lib/constants/Colors";
import {useFlashMessage} from "@/lib/stores/flashMessage/useFlashStore";
import AsyncStorage from "@react-native-async-storage/async-storage";
import {useAuthStore} from "@/lib/stores/auth/useAuthStore";

export default function Register() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const flash = useFlashMessage();
    const {initialize} = useAuthStore();

    const handleRegister = async () => {
        if (!email || !password || !confirmPassword || !firstName || !lastName) {
            flash.error('Veuillez remplir tous les champs.');
            return;
        }

        if (password !== confirmPassword) {
            flash.error('Les mots de passe ne correspondent pas.');
            return;
        }

        try {
            const response = await api.post("/auth/register", {
                email,
                password,
                firstName,
                lastName,
                role: "EMPLOYEE"
            });

            const token = response.data.jwt;
            await AsyncStorage.setItem('userToken', token);
            initialize();

            flash.success('Inscription réussie !');
            setTimeout(() => {
                router.replace("/login");
            }, 2000);

        } catch (error) {
            console.error("Erreur lors de l'inscription :", error);
            flash.error("Une erreur s'est produite lors de l'inscription.");
        }
    };

    function redirection(): void {
        router.replace("/login");
    }

    return (
        <SafeAreaView style={styles.container}>
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
                <KeyboardAvoidingView
                    behavior={Platform.OS === "ios" ? "padding" : "height"}
                    style={styles.inner}
                >
                    <Text style={styles.title}>Trinity</Text>
                    <Text style={styles.subtitle}>Inscription</Text>

                    <View style={styles.inputContainer}>
                        <Input placeholder="Email" value={email} onChangeText={setEmail}/>
                        <Input placeholder="Prénom" value={firstName} onChangeText={setFirstName}/>
                        <Input placeholder="Nom" value={lastName} onChangeText={setLastName}/>
                        <Input placeholder="Mot de passe" value={password} onChangeText={setPassword} isPassword/>
                        <Input placeholder="Confirmer le mot de passe" value={confirmPassword}
                               onChangeText={setConfirmPassword} isPassword/>
                    </View>

                    <View style={styles.containerButton}>
                        <Button title="S'inscrire" color="primary" action={handleRegister} size="full"/>
                        <Text style={styles.text}>
                            Vous avec déjà un compte ? <Link style={styles.link} href={'/login'}>Connectez vous</Link>
                        </Text>
                    </View>
                </KeyboardAvoidingView>
            </TouchableWithoutFeedback>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.secondary,
    },
    inner: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        paddingHorizontal: 20,
    },
    title: {
        fontSize: 50,
        fontFamily: 'CabinetGrotesk-ExtraBold',
        color: colors.primary,
        marginBottom: 10,
    },
    subtitle: {
        fontSize: 20,
        color: "#666",
        marginBottom: 20,
    },
    inputContainer: {
        width: "100%",
        marginBottom: 20,
    },
    containerButton: {
        width: "100%",
    },
    text: {
        textAlign: "center",
        marginTop: 10,
        marginBottom: 10,
    },
    link: {
        color: colors.primary,
        textDecorationLine: "underline",
    }
});
