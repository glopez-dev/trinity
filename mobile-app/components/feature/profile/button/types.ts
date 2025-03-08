import {icons} from "lucide-react-native";

export interface ProfileButtonProps {
    title: string;
    link: string;
    icon: keyof typeof icons;
}