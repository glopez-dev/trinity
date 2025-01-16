import React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { NavigationContent } from './NavigationContent';
import styles from '../styles/Sidebar.module.css';
import {NavLogo} from "@/components/ui/navigation/NavLogo";
import {NavProps} from "@/components/layout/navigation/components/types";

export const Sidebar: React.FC<NavProps> = ({ isOpen, onToggle }) => {
    return (
        <div className={`${styles.sidebar} ${!isOpen ? styles.collapsed : ''}`}>
            <div className={styles.header}>
                <NavLogo isCollapsed={!isOpen}/>
                <button
                    onClick={onToggle}
                    className={styles.toggleButton}
                    aria-label={isOpen ? "Réduire le menu" : "Agrandir le menu"}
                >
                    {isOpen ? <ChevronLeft size={24} /> : <ChevronRight size={24} />}
                </button>
            </div>

            <NavigationContent isCollapsed={!isOpen} />
        </div>
    );
};