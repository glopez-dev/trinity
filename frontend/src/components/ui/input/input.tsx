import React, {ChangeEvent, FC, useState} from 'react';
import styles from './styles.module.css';
import {InputProps} from "@/components/ui/input/types";


const Input: FC<InputProps> = ({
                                   type = 'text',
                                   placeholder = 'Saisir...',
                                   label = '',
                                   onChange,
                                   value,
                                   name,
                                   regex,
                                   required = false,
                               }) => {

    const [error, setError] = useState<string | null>(null);

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const inputValue = e.target.value;

        if (required && !inputValue) {
            setError('Ce champ est obligatoire.');
        } else if (regex && !regex.test(inputValue)) {
            setError('Le format est invalide.');
        } else {
            setError(null);
        }

        if (onChange) {
            onChange(e);
        }
    };


    return (
        <div className={styles.inputContainer}>
            {label && <label className={styles.label}>{label}</label>}
            <input
                role={'inputComponent'}
                type={type}
                placeholder={placeholder}
                className={`${styles.input} ${error !== null ? styles.inputError : ''}`}
                onChange={handleInputChange}
                value={value}
                name={name}
                required={required}
                pattern={regex?.source}
            />
            {error && <span className={styles.error}>{error}</span>}
        </div>


    );
};

export default Input;
