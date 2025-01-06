import React, { ChangeEvent, FC } from 'react';
import styles from './styles.module.css';

interface InputProps {
  type?: string;
  placeholder?: string; 
  className?: string; 
  onChange?: (e: ChangeEvent<HTMLInputElement>) => void; 
  value?: string | number;
  name?: string; 
}

const Input: FC<InputProps> = ({
  type = "text",
  placeholder = "Saisir...",
  className = "",
  onChange,
  value,
  name,
}) => {
  return (
      <input
        type={type}
        placeholder={placeholder}
        className={`${styles.input} ${className}`}
        onChange={onChange}
        value={value}
        name={name}
      />
  );
};

export default Input;
