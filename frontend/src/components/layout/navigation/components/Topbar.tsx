import React from 'react';
import { Menu, X } from 'lucide-react';
import { NavigationContent } from './NavigationContent';
import styles from '../styles/Topbar.module.css';

interface TopbarProps {
    isOpen: boolean;
    onToggle: () => void;
}

export const Topbar: React.FC<TopbarProps> = ({ isOpen, onToggle }) => {
    return (
        <div className={styles.topbar}>
            <div className={styles.header}>
                <img
                    src="/images/logo-svg-trinity.svg"
                    alt="Trinity"
                    className={styles.logo}
                />
                <button
                    onClick={onToggle}
                    className={styles.menuButton}
                    aria-label={isOpen ? "Fermer le menu" : "Ouvrir le menu"}
                >
                    {isOpen ? <X size={24} /> : <Menu size={24} />}
                </button>
            </div>

            {isOpen && (
                <div className={styles.menu}>
                    <NavigationContent isCollapsed={!isOpen} onToggle={onToggle}  />
                </div>
            )}
        </div>
    );
};