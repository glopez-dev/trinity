import React from 'react';
import styles from './NavLogo.module.css';
import Image from "next/image";

interface NavLogoProps {
    isCollapsed?: boolean;
}

export const NavLogo: React.FC<NavLogoProps> = ({ isCollapsed }) => (
    <Image
        src="/images/logo-svg-trinity.svg"
        alt="Trinity Logo"
        className={`${styles.logo} ${isCollapsed ? styles.collapsed : ''}`}
    />
);