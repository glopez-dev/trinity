import {FC, MouseEvent} from 'react';
import styles from './button.module.css';
import Icon from '@/components/ui/icons/Icon';
import {icons} from "lucide-react";

type ButtonType = 'primary' | 'secondary' | 'accent';
type ButtonSize = 'full';

interface ButtonProps {
    title: string;
    type?: ButtonType;
    action?: (event: MouseEvent<HTMLButtonElement>) => void;
    size?: ButtonSize;
    icon?: keyof typeof icons | null;
    disabled?: boolean;
}

const Button: FC<ButtonProps> = ({
                                     title,
                                     type = 'primary',
                                     action,
                                     size = '',
                                     icon = null,
                                     disabled = false
                                 }) => {
    let color = '';
    switch (type) {
        case 'secondary':
            color = '#4A6741';
            break;
        default:
            color = '#F5F1E8';
            break;
    }
    return (
        <div>
            <button
                className={`${styles[type]} ${size ? styles.full : ''}`}
                onClick={action}
                type="button"
                disabled={disabled}
            >
                <p>{title}</p>
                {icon && <Icon name={icon} size={20} color={color}/>}
            </button>
        </div>
    );
};

export default Button;