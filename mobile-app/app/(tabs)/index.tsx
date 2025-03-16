import {SafeAreaView, Text, View} from "react-native";
import React, {useEffect} from "react";
import {getProducts} from "@/lib/api/products/productsCall";

export default function Home() {
    const [products, setProducts] = React.useState([]);

    useEffect(() => {
        const fetchProducts = async () => {
            getProducts().then(data => {
                setProducts(data);
            })
            console.log('products', products)
        }

        fetchProducts();
    }, [])


    return (
        <SafeAreaView>
            <View>
                <Text>Home</Text>
            </View>
        </SafeAreaView>
    );
}
