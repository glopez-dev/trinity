import React, {useEffect, useRef, useState} from 'react';
import styles from './styles.module.css';
import {Option, SearchableSelectProps} from "@/components/ui/input/select/types";
import {ChevronDown} from "lucide-react";


const SearchableSelect: React.FC<SearchableSelectProps> = ({
                                                               options = [],
                                                               onSelect,
                                                               placeholder = 'Rechercher...'
                                                           }) => {
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [searchTerm, setSearchTerm] = useState<string>('');
    const [selectedOption, setSelectedOption] = useState<Option | null>(null);
    const wrapperRef = useRef<HTMLDivElement>(null);
    const inputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const filteredOptions = options.filter(option =>
        option.label.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleSelect = (option: Option) => {
        setSelectedOption(option);
        setSearchTerm(option.label);
        setIsOpen(false);
        if (onSelect) onSelect(option);
    };

    const handleInputClick = () => {
        setIsOpen(true);
        if (selectedOption) {
            setSearchTerm(selectedOption.label);
            inputRef.current?.select();
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(e.target.value);
        setSelectedOption(null);
        setIsOpen(true);
    };

    return (
        <div className={styles.wrapper} ref={wrapperRef}>
            <div className={styles.inputWrapper}>
                <input
                    ref={inputRef}
                    type="text"
                    className={styles.input}
                    placeholder={placeholder}
                    value={searchTerm}
                    onChange={handleInputChange}
                    onClick={handleInputClick}
                />
                <ChevronDown
                    onClick={() => setIsOpen(!isOpen)}
                    className={`${styles.arrow} ${isOpen ? styles.arrowOpen : ''}`}
                    size={20}
                    color={'#757575'}
                />
            </div>

            {isOpen && filteredOptions.length > 0 && (
                <div className={styles.dropdown}>
                    <div className={styles.optionsList}>
                        {filteredOptions.map((option) => (
                            <div
                                key={option.value}
                                className={styles.option}
                                onClick={() => handleSelect(option)}
                            >
                                {option.label}
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default SearchableSelect;