import {Tabs} from 'expo-router';
import styles from '../styles/tabStyles'
import { Home, Scan,History,  ShoppingCart, User } from 'lucide-react-native';

export default function TabLayout() {
    return (
 
        <Tabs screenOptions={{tabBarStyle: styles.tabar}}  >
            <Tabs.Screen name="index" options={{title: 'Accueil', headerShown: false, tabBarIcon: ({ color, size }) => <Home color={color} size={'32'}  />,  tabBarActiveTintColor: 'black', tabBarInactiveTintColor: 'white',  }} />
            <Tabs.Screen name="cart" options={{title: 'Panier', headerShown: false,tabBarIcon: ({ color, size }) => <ShoppingCart color={color} size={'32'} />,  tabBarActiveTintColor: 'black', tabBarInactiveTintColor: 'white',}}/>
            <Tabs.Screen name="scan" options={{title: 'Scanner', headerShown: false,tabBarIcon: ({ color, size }) => <Scan color={color} size={'32'} />,  tabBarActiveTintColor: 'black', tabBarInactiveTintColor: 'white',  }}/>
            <Tabs.Screen name="history" options={{title: 'Historique', headerShown: false, tabBarIcon: ({ color, size }) => <History color={color} size={'32'} />,  tabBarActiveTintColor: 'black', tabBarInactiveTintColor: 'white',}}/>
            <Tabs.Screen name="profile" options={{title: 'Profil', headerShown: false,tabBarIcon: ({ color, size }) => <User color={color} size={'32'} />,  tabBarActiveTintColor: 'black', tabBarInactiveTintColor: 'white',}}/>
        </Tabs>
       
    );
}