import {Tabs} from 'expo-router';

export default function TabLayout() {
    return (
        <Tabs>
            <Tabs.Screen name="index" options={{title: 'Accueil', headerShown: false}}/>
            <Tabs.Screen name="cart" options={{title: 'Panier', headerShown: false}}/>
            <Tabs.Screen name="scan" options={{title: 'Scanner', headerShown: false}}/>
            <Tabs.Screen name="history" options={{title: 'Historique', headerShown: false}}/>
            <Tabs.Screen name="profile" options={{title: 'Profil', headerShown: false}}/>
        </Tabs>
    );
}