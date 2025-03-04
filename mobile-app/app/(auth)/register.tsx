import { 
    SafeAreaView, 
    Text, 
    View, 
    TextInput, 
    TouchableOpacity, 
    StyleSheet, 
    KeyboardAvoidingView, 
    Platform, 
    TouchableWithoutFeedback, 
    Keyboard,
    Alert
} from "react-native";
import { useState } from "react";
import { api } from "@/lib/API/api"; 

export default function Register() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleRegister = async () => {
        if (!email || !password || !confirmPassword) {
            Alert.alert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (password !== confirmPassword) {
            Alert.alert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        try {
            const response = await api.post("/auth/register", {
                email,
                password
            });

            console.log("Réponse API :", response.data);
            Alert.alert("Succès", "Inscription réussie !");
            
            
        } catch (error) {
            console.error("Erreur lors de l'inscription :", error);
            Alert.alert("Erreur", "Une erreur s'est produite lors de l'inscription., ");

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
                    <Text style={styles.subtitle}>Inscription</Text>

                    <View style={styles.inputContainer}>
                        <TextInput 
                            style={styles.input} 
                            placeholder="Email" 
                            keyboardType="email-address" 
                            value={email}
                            onChangeText={setEmail}
                            autoCapitalize="none"
                        />
                        <TextInput 
                            style={styles.input} 
                            placeholder="Mot de passe" 
                            secureTextEntry 
                            value={password}
                            onChangeText={setPassword}
                        />
                        <TextInput 
                            style={styles.input} 
                            placeholder="Confirmer le mot de passe" 
                            secureTextEntry 
                            value={confirmPassword}
                            onChangeText={setConfirmPassword}
                        />
                    </View>

                    <TouchableOpacity style={styles.button} onPress={handleRegister}>
                        <Text style={styles.buttonText}>S'inscrire</Text>
                    </TouchableOpacity>
                </KeyboardAvoidingView>
            </TouchableWithoutFeedback>
        </SafeAreaView>
    );
}

// Styles
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
    input: {
        backgroundColor: "#fff",
        padding: 12,
        borderRadius: 8,
        marginBottom: 10,
        borderWidth: 1,
        borderColor: "#ccc",
    },
    button: {
        backgroundColor: "#4A6741",
        paddingVertical: 12,
        paddingHorizontal: 20,
        borderRadius: 8,
    },
    buttonText: {
        color: "#fff",
        fontSize: 16,
        fontWeight: "bold",
    },
});
