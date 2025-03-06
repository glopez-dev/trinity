import React, { createContext, useState, useEffect, ReactNode } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { jwtDecode } from "jwt-decode";


interface User {
  id: string;
  name: string;
  email: string;
}


interface AuthContextType {
  user: User | null;
  loading: boolean;
  logout: () => Promise<void>;
}


export const AuthContext = createContext<AuthContextType | undefined>(undefined);


interface AuthProviderProps {
  children: ReactNode;
}


export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

 
  useEffect(() => {
    const checkUser = async () => {
      const token = await AsyncStorage.getItem("userToken");
      if (token) {
        try {
          const decodedUser: User = jwtDecode<User>(token);
          setUser(decodedUser);
          console.log(user);

        } catch (error) {
          console.log("Token invalide");
        
        }
      }
      setLoading(false);
    };

    checkUser();
  }, []);

  

 
  const logout = async () => {
    await AsyncStorage.removeItem("userToken");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading,  logout }}>
      {children}
    </AuthContext.Provider>
  );
};
