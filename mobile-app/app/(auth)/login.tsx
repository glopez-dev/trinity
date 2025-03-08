import {
    SafeAreaView,
    Text,
    View,
    StyleSheet,
    KeyboardAvoidingView,
    Platform,
    TouchableWithoutFeedback,
    Keyboard, Image
} from "react-native";
import { useState } from "react";
import {Link, useRouter} from "expo-router";
import { api } from "@/lib/API/api";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Button from "@/components/ui/buttons/Button"; 
import Input from "@/components/ui/input/Input";
import {useFlashMessage} from "@/lib/stores/flashMessage/useFlashStore";
import {colors} from "@/lib/constants/Colors";
import {useAuthStore} from "@/lib/stores/auth/useAuthStore";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const router = useRouter();
    const flash = useFlashMessage();
    const {initialize} = useAuthStore();

    const handleLogin = async () => {
        if (!email || !password) {
            flash.error("Veuillez remplir tous les champs.");
            return;
        }

        try {
            const response = await api.post("/auth/login", { email, password });
            const token = response.data.jwt;
            await AsyncStorage.setItem('userToken', token);
            initialize();
            flash.success( "Connexion réussie !");

            setTimeout(() => {
                router.replace("/");
            }, 2000);
        } catch (error) {
            flash.error('Une erreur s\'est produite lors de la connexion.');
        }
    };

    return (
        <SafeAreaView style={styles.container}>
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
                <KeyboardAvoidingView 
                    behavior={Platform.OS === "ios" ? "padding" : "height"}
                    style={styles.inner}
                >

                    <Text style={styles.title}>Trinity</Text>
                    <Text style={styles.subtitle}>Connexion</Text>

                    <View style={styles.inputContainer}>
                        <Input 
                            placeholder="Email"
                            value={email}
                            onChangeText={setEmail}
                        />
                        <Input 
                            placeholder="Mot de passe"
                            value={password}
                            onChangeText={setPassword}
                            isPassword
                        />
                    </View>

                    <View style={styles.containerButton}>
                        <Button title="Login" color="primary" action={handleLogin} size="full" />
                        <Text style={styles.text}>
                            Vous n'avez pas de compte ? <Link style={styles.link} href={'/register'}>S'inscrire</Link>
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
        backgroundColor: "#F5F1E8",
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
        color: "#4A6741",
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

