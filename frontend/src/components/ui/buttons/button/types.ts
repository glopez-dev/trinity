import {icons} from "lucide-react";

type ButtonColor = 'primary' | 'secondary' | 'accent';
type ButtonSize = 'full';
type ButtonType = 'button' | 'submit' | 'reset';

export interface ButtonProps {
    title: string;
    color?: ButtonColor;
    action?: () => void;
    size?: ButtonSize;
    icon?: keyof typeof icons | null;
    disabled?: boolean;
    type?: ButtonType;
}