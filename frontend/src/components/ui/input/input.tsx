import React, {FC, useState} from 'react';
import styles from './styles.module.css';
import {InputProps} from "@/components/ui/input/types";


const Input: FC<InputProps> = ({
                                   type = 'text',
                                   placeholder = 'Saisir...',
                                   label = '',
                                   onChange,
                                   value,
                                   name,
                                   required = false,
                                   schema
                               }) => {

    const [error, setError] = useState<string | null>(null);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputValue = e.target.value;
        if (schema) {
            const parsed = schema.safeParse(inputValue);
            if (!parsed.success) {
                setError(parsed.error.issues[0].message);
            } else {
                setError(null);
            }
        }
        if(onChange) {
            onChange(inputValue);
        }
    };


    return (
        <div className={styles.inputContainer}>
            {label && <label aria-label={name} className={styles.label}>{label}</label>}
            <input
                role={'inputComponent'}
                type={type}
                placeholder={placeholder}
                className={`${styles.input} ${error !== null ? styles.inputError : ''}`}
                onChange={handleInputChange}
                value={value}
                name={name}
                required={required}
            />
            {error && <span className={styles.error}>{error}</span>}
        </div>


    );
};

export default Input;
