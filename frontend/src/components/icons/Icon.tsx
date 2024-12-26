import { icons, LucideProps, LucideIcon } from 'lucide-react';
import {JSX} from "react";

interface IconProps extends Omit<LucideProps, 'ref'> {
    name: keyof typeof icons;
}

const Icon = ({ name, ...props }: IconProps): JSX.Element => {
    const LucideIcon: LucideIcon = icons[name];
    return <LucideIcon {...props} />;
};

export default Icon;