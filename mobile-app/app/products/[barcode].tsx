import {SafeAreaView, Text} from "react-native";
import {useLocalSearchParams} from "expo-router";
import {useEffect} from "react";

export default function ModalProductDetails() {
    const {barcode} = useLocalSearchParams();

    useEffect(() => {
        if (barcode) {
            // Fetch product details
        }
    }, [barcode]);

    return (
        <SafeAreaView>
            <Text>
                {barcode}
            </Text>
        </SafeAreaView>
    )
}