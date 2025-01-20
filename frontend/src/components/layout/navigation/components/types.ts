import {icons} from "lucide-react";

export interface NavigationContentProps {
    isCollapsed?: boolean;
    onToggle?: () => void;
}

export interface NavItemProps {
    name: string;
    icon: keyof typeof icons;
    path: string;
}

export interface NavProps {
    isOpen: boolean;
    onToggle: () => void;
}