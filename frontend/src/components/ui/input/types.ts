import {ChangeEvent} from "react";


export interface InputProps {
    type?: string;
    placeholder?: string;
    label?: string;
    onChange?: (e: ChangeEvent<HTMLInputElement>) => void;
    value?: string | number;
    name: string;
    required?: boolean;
    regex?: RegExp;
}
