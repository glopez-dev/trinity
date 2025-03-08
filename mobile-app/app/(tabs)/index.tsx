import { Text, Pressable, SafeAreaView, Button } from "react-native";
import { Link } from "expo-router";
import React, { useContext } from "react";
import { AuthContext } from "@/context/AuthProvider";

export default function Home() {
  const auth = useContext(AuthContext);

  if (!auth) {
    return <Text>Erreur: AuthProvider non trouvé</Text>;
  }

  const { user, logout } = auth;

  return (
    <SafeAreaView>
      {!user ? (
        <>
          <Link href="/login" asChild>
            <Pressable>
              <Text>Login</Text>
            </Pressable>
          </Link>
          <Link href="/register" asChild>
            <Pressable>
              <Text>Register</Text>
            </Pressable>
          </Link>
        </>
      ) : (
        <>
          <Text>Bienvenue, {user.sub}</Text>
          <Button title="Se déconnecter" onPress={logout} />
        </>
      )}
    </SafeAreaView>
  );
}
