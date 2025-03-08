import { 
    SafeAreaView, 
    Text, 
    View, 
    KeyboardAvoidingView, 
    Platform, 
    TouchableWithoutFeedback, 
    Keyboard,
    StyleSheet
} from "react-native";
import { useState } from "react";
import { api } from "@/lib/API/api"; 
import { router } from "expo-router";
import Button from "@/components/ui/buttons/Button"; 
import Input from "@/components/ui/input/Input";
import { FlashMessage } from "@/components/ui/flashMessage/FlashMessage";
import { MessageType } from "@/lib/types/flashMessage/types";

export default function Register() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [firstName, setFirstName] = useState("");  
    const [lastName, setLastName] = useState("");    
    const [flashMessage, setFlashMessage] = useState<{ message: string, type: MessageType } | null>(null);

    const handleRegister = async () => {
        if (!email || !password || !confirmPassword || !firstName || !lastName) {
            setFlashMessage({ message: "Veuillez remplir tous les champs.", type: "error" });
            return;
        }

        if (password !== confirmPassword) {
            setFlashMessage({ message: "Les mots de passe ne correspondent pas.", type: "error" });
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

            console.log("Réponse API :", response.data);
            setFlashMessage({ message: "Inscription réussie !", type: "success" });

            setTimeout(() => {
                router.replace("/login");
            }, 2000);
            
        } catch (error) {
            console.error("Erreur lors de l'inscription :", error);
            setFlashMessage({ message: "Une erreur s'est produite lors de l'inscription.", type: "error" });
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
                    {flashMessage && (
                        <FlashMessage 
                            message={flashMessage.message} 
                            type={flashMessage.type} 
                            onClose={() => setFlashMessage(null)}
                        />
                    )}

                    <Text style={styles.title}>Trinity</Text>
                    <Text style={styles.subtitle}>Inscription</Text>

                    <View style={styles.inputContainer}>
                        <Input placeholder="Email" value={email} onChangeText={setEmail} />
                        <Input placeholder="Prénom" value={firstName} onChangeText={setFirstName} />
                        <Input placeholder="Nom" value={lastName} onChangeText={setLastName} />
                        <Input placeholder="Mot de passe" value={password} onChangeText={setPassword} isPassword />
                        <Input placeholder="Confirmer le mot de passe" value={confirmPassword} onChangeText={setConfirmPassword} isPassword />
                    </View>

                    <View style={styles.containerButton}>
                        <Button title="S'inscrire" color="primary" action={handleRegister} size="full" />
                        <Button title="Login" color="primary" action={redirection} size="full" />
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
