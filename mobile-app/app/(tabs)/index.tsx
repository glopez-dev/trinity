import { SafeAreaView, Text, Pressable, SafeAreaView } from "react-native";
import { Link } from "expo-router";
import React from "react";

export default function Home() {
  return (
    <SafeAreaView>
      <Link href="/(auth)/login" asChild>
        <Pressable>
          <Text>login</Text>
        </Pressable>
      </Link>
      <Link href="/(auth)/register" asChild>
        <Pressable>
          <Text>Register</Text>
        </Pressable>
      </Link>
      <Text>kkeekekek</Text>
    </SafeAreaView>
  );
}
