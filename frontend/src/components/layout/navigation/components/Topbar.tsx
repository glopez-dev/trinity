import React from 'react';
import { Menu, X } from 'lucide-react';
import { NavigationContent } from './NavigationContent';
import styles from '../styles/Topbar.module.css';
import {NavLogo} from "@/components/ui/navigation/NavLogo";
import {NavProps} from "@/components/layout/navigation/components/types";

export const Topbar: React.FC<NavProps> = ({ isOpen, onToggle }) => {
    return (
        <div className={styles.topbar}>
            <div className={styles.header}>
                <NavLogo />
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