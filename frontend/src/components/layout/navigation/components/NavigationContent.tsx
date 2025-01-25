import React from 'react';
import styles from '../styles/NavigationContent.module.css';
import NavItem from "@/components/ui/navigation/NavItem";
import {NavigationContentProps, NavItemProps} from "@/components/layout/navigation/components/types";


const navItems: NavItemProps[] = [
    {name: 'Dashboard', icon: "LayoutDashboard", path: '/dashboard'},
    {name: 'Produits', icon: "Package", path: '/products'},
    {name: 'Employés', icon: "UserCog", path: '/users/employees'},
    {name: 'Clients', icon: "Users", path: '/users/customers'},
    {name: 'Factures', icon: "FileText", path: '/invoices'},
    {name: 'Rapports', icon: "ChartNoAxesColumnIncreasing", path: '/reports'},
];

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