import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Bell } from 'lucide-react-native';

const Header = () => {
  return (
    <View style={styles.header}>
      <Text style={styles.title}>Trinity</Text>
      <Bell size={30} color="white" />
    </View>
  );
};

const styles = StyleSheet.create({
  header: {
    backgroundColor: '#4A6741',
    justifyContent: 'space-between',  
    alignItems: 'center',            
    height: 100,
    flexDirection: 'row',
    paddingHorizontal: 20,          
    paddingTop: 30,                   
  },
  title: {
    color: 'white',
    fontSize: 40,
    fontWeight: 'bold',
  },
});

export default Header;
