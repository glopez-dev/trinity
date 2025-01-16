import React from 'react';
import styles from './NavItem.module.css';
import {icons} from "lucide-react";
import Icon from "@/components/ui/icons/Icon";
import {useRouter, usePathname} from "next/navigation";

interface NavItemProps {
    icon: keyof typeof icons;
    label: string;
    route: string;
    isCollapsed?: boolean;
    onToggle?: () => void;
}


export const NavItem: React.FC<NavItemProps> = ({icon, label, route, isCollapsed, onToggle}) => {
    const router = useRouter();
    const pathname = usePathname();
    return (
        <button className={`${styles.navButton} ${route === pathname && styles.navButtonActive}`} onClick={() => {
            if (onToggle) {
                onToggle()
            }
            router.push(route)
        }}>
            <span className={styles.navIcon}>
                <Icon name={icon} size={24}/>
            </span>
            {!isCollapsed && <span className={styles.navText}>{label}</span>}
        </button>
    )
};

export default NavItem;