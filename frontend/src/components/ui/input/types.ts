import { z } from "zod";

export type InputValueTypes = string | number;

export interface InputProps {
    type?: string;
    placeholder?: string;
    label?: string;
    onChange: (value: InputValueTypes) => void;
    value: InputValueTypes;
    name: string;
    required?: boolean;
    schema?: z.ZodString;
}
