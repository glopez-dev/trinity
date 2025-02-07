import { FC } from 'react';
import styles from './icon-button.module.css';
import {icons} from "lucide-react";
import Icon from '@/components/ui/icons/Icon';

interface IconButtonProps {
    icon: keyof typeof icons;
    onClick: () => void;
}

const IconButton: FC<IconButtonProps> = ({ icon, onClick }) => {
    return (
        <button data-testid={`icon-button-${icon}`} className={styles.iconButton} onClick={onClick}>
            <Icon name={icon} size={20} className={styles.iconButtonIcon}/>
        </button>
    );
};

export default IconButton;