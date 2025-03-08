import { Tabs } from 'expo-router';
import { Text } from 'react-native';
import styles from '../styles/tabStyles';
import { AuthProvider } from "@/context/AuthProvider";
import { Home, ScanBarcode, History, ShoppingCart, User } from 'lucide-react-native';


export default function TabLayout() {
    return (
 <AuthProvider>       <Tabs screenOptions={{ tabBarStyle: styles.tabar }}>
            <Tabs.Screen
                name="index"
                options={{
                    title: 'Accueil',
                    headerShown: false,
                    tabBarIcon: ({ color, size, focused }) => (
                        <Home color={color} size={focused ? 27 : 25} />
                    ),
                    tabBarActiveTintColor: 'white',
                    tabBarInactiveTintColor: 'white',
                    tabBarLabel: ({ focused }) => (
                        <Text style={{ fontWeight: focused ? 'bold' : 'normal',paddingTop:5,color:'white',fontSize: 13 }}>
                            Accueil
                        </Text>
                    ),
                }}
            />
            <Tabs.Screen
                name="cart"
                options={{
                    title: 'Panier',
                    headerShown: false,
                    tabBarIcon: ({ color, size, focused }) => (
                        <ShoppingCart color={color} size={focused ? 27 : 25} />


                    ),
                    tabBarActiveTintColor: 'white',
                    tabBarInactiveTintColor: 'white',
                    tabBarLabel: ({ focused }) => (
                        <Text style={{ fontWeight: focused ? 'bold' : 'normal', color:'white',paddingTop:5,fontSize: 13 }}>
                            Panier
                        </Text>
                    ),
                }}
            />
            <Tabs.Screen
                name="scan"
                options={{
                    title: 'Scanner',
                    headerShown: false,
                    tabBarIcon: ({ color, size, focused }) => (
                        <ScanBarcode color={color} size={focused ? 27 : 25} />
                    ),
                    tabBarActiveTintColor: 'white',
                    tabBarInactiveTintColor: 'white',
                    tabBarLabel: ({ focused }) => (
                        <Text style={{ fontWeight: focused ? 'bold' : 'normal',color:'white',paddingTop:5,fontSize: 13  }}>
                            Scanner
                        </Text>
                    ),
                }}
            />
            <Tabs.Screen
                name="history"
                options={{
                    title: 'Historique',
                    headerShown: false,
                    tabBarIcon: ({ color, size, focused }) => (
                        <History color={color} size={focused ? 27 : 25} />
                    ),
                      tabBarActiveTintColor: 'white',
                    tabBarInactiveTintColor: 'white',
                    tabBarLabel: ({ focused }) => (
                        <Text style={{ fontWeight: focused ? 'bold' : 'normal',color:'white',paddingTop:5, fontSize: 13  }}>
                            Historique
                        </Text>
                    ),
                }}
            />
            <Tabs.Screen
                name="profile"
                options={{
                    title: 'Profil',
                    headerShown: false,
                    tabBarIcon: ({ color, size, focused }) => (
                        <User color={color} size={focused ? 27 : 25} />
                    ),
                    tabBarActiveTintColor: 'white',
                    tabBarInactiveTintColor: 'white',
                    tabBarLabel: ({ focused }) => (
                        <Text style={{ fontWeight: focused ? 'bold' : 'normal',color:'white',paddingTop:5 ,fontSize: 13 }}>
                            Profil
                        </Text>
                    ),
                }}
            />
        </Tabs>
        </AuthProvider>

    );
}
