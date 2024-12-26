import React from 'react';
import {icons, LogOut} from 'lucide-react';
import styles from '../styles/NavigationContent.module.css';
import NavItem from "@/components/ui/navigation/NavItem";

interface NavItemProps {
    name: string;
    icon: keyof typeof icons;
    path: string;
}

const navItems: NavItemProps[] = [
    {name: 'Dashboard', icon: "LayoutDashboard", path: '/dashboard'},
    {name: 'Produits', icon: "Package", path: '/products'},
    {name: 'Clients', icon: "Users", path: '/users'},
    {name: 'Factures', icon: "FileText", path: '/invoices'},
    {name: 'Rapports', icon: "ChartNoAxesColumnIncreasing", path: '/reports'},
];

interface NavigationContentProps {
    isCollapsed?: boolean;
    onToggle?: () => void;
}

export const NavigationContent: React.FC<NavigationContentProps> = ({isCollapsed, onToggle}) => {

    return (
        <div className={styles.wrapper}>
            <nav className={styles.navigation}>
                <ul className={styles.navList}>
                    {navItems.map((item, index) => (
                        <NavItem
                            key={index}
                            label={item.name}
                            icon={item.icon}
                            isCollapsed={isCollapsed}
                            route={item.path}
                            onToggle={onToggle}
                        />
                    ))}
                </ul>
            </nav>

            <div className={styles.footer}>
                <NavItem
                    icon="LogOut"
                    label="Déconnexion"
                    route="/"
                    isCollapsed={isCollapsed}
                />
            </div>
        </div>
    );
};