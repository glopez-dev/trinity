import {icons, LucideProps} from 'lucide-react-native';

interface IconProps extends LucideProps {
    name: keyof typeof icons;
    color: string;
    testID?: string;
}

const Icon = ({ name, ...props }: IconProps) => {
    const LucideIcon = icons[name];
    return <LucideIcon {...props} />;
};

export default Icon;