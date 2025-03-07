import { 
    SafeAreaView, 
    Text, 
    View,
    StyleSheet, 
    KeyboardAvoidingView, 
    Platform, 
    TouchableWithoutFeedback, 
    Keyboard
} from "react-native";
import { useState } from "react";
import { useRouter } from "expo-router";
import { api } from "@/lib/API/api";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Button from "@/components/ui/buttons/Button"; 
import Input from "@/components/ui/input/Input";
import { FlashMessage } from "@/components/ui/flashMessage/FlashMessage";
import { MessageType } from "@/lib/types/flashMessage/types";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [flashMessage, setFlashMessage] = useState<{ message: string, type: MessageType } | null>(null);
    const router = useRouter();  

    const handleLogin = async () => {
        if (!email || !password) {
            setFlashMessage({ message: "Veuillez remplir tous les champs.", type: "error" });
            return;
        }

        try {
            const response = await api.post("/auth/login", { email, password });
            const token = response.data.jwt;
            await AsyncStorage.setItem('userToken', token);

            setFlashMessage({ message: "Connexion réussie !", type: "success" });

            setTimeout(() => {
                router.replace("/");
            }, 2000);
        } catch (error) {
            console.error("Erreur lors de la connexion :", error);
            setFlashMessage({ message: "Une erreur s'est produite lors de la connexion.", type: "error" });
        }
    };

    function redirection() {
        router.replace("/register");
    }

    return (
        <SafeAreaView style={styles.container}>
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
                <KeyboardAvoidingView 
                    behavior={Platform.OS === "ios" ? "padding" : "height"}
                    style={styles.inner}
                >
                    {flashMessage && (
                        <FlashMessage 
                            message={flashMessage.message} 
                            type={flashMessage.type} 
                            onClose={() => setFlashMessage(null)}
                        />
                    )}

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
                        <Button title="S'inscrire" color="primary" action={redirection} size="full" />
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
        fontSize: 32,
        fontWeight: "bold",
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
});

