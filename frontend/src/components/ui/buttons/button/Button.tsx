import {FC} from 'react';
import styles from './button.module.css';
import Icon from '@/components/ui/icons/Icon';
import {ButtonProps} from "@/components/ui/buttons/button/types";


const Button: FC<ButtonProps> = ({
                                     title,
                                     color = 'primary',
                                     action,
                                     size = '',
                                     icon = null,
                                     disabled = false,
                                     type = 'button'
                                 }) => {
    let iconColor = '';
    switch (color) {
        case 'secondary':
            iconColor = '#4A6741';
            break;
        default:
            iconColor = '#F5F1E8';
            break;
    }

    return (
        <div>
            <button
                className={`${styles[color]} ${size ? styles.full : ''}`}
                onClick={action}
                type={type}
                disabled={disabled}
            >
                <p>{title}</p>
                {icon && <Icon name={icon} size={20} color={iconColor}/>}
            </button>
        </div>
    );
};

export default Button;