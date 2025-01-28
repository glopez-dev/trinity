export interface Option {
    value: string;
    label: string;
}

export interface SearchableSelectProps {
    options: Option[];
    onSelect?: (option: Option) => void;
    placeholder?: string;
}