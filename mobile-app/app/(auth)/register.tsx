import { 
    SafeAreaView, 
    Text, 
    View, 
    KeyboardAvoidingView, 
    Platform, 
    TouchableWithoutFeedback, 
    Keyboard,
    Alert,
    StyleSheet
} from "react-native";
import { useState } from "react";
import { api } from "@/lib/API/api"; 
import { router } from "expo-router";
import Button from "@/components/ui/buttons/Button"; 
import Input from "@/components/ui/input/Input";

export default function Register() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [firstName, setFirstName] = useState("");  
    const [lastName, setLastName] = useState("");    

    const handleRegister = async () => {
        if (!email || !password || !confirmPassword || !firstName || !lastName) {
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
                password,
                firstName,
                lastName,
                role: "EMPLOYEE"  
            });

            console.log("Réponse API :", response.data);
            Alert.alert("Succès", "Inscription réussie !");
            router.replace("/login");
            
        } catch (error) {
            console.error("Erreur lors de l'inscription :", error);
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
                        <Input 
                            placeholder="Email"
                            value={email}
                            onChangeText={setEmail}
                          
                        />
                        <Input 
                            placeholder="Prénom"
                            value={firstName}
                            onChangeText={setFirstName}
                        />
                        <Input 
                            placeholder="Nom"
                            value={lastName}
                            onChangeText={setLastName}
                        />
                        <Input 
                            placeholder="Mot de passe"
                            value={password}
                            onChangeText={setPassword}
                            isPassword
                            
                        />
                        <Input 
                            placeholder="Confirmer le mot de passe"
                            value={confirmPassword}
                            onChangeText={setConfirmPassword}
                            isPassword
                           
                        />
                    </View>

                    <View style={styles.containerbuton}>

                    <Button 
                        title="S'inscrire"
                        color="primary" 
                        action={handleRegister} 
                        size="full"  
                    />
                    <Button 
                        title="Login"
                        color="primary"  
                        action={redirection} 
                        size="full"  
                    />
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
       
    },
    containerbuton: {
        width: "100%",
        marginTop: 10,
    },

    

   

});
