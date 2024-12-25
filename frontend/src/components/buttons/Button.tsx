import { FC, MouseEvent } from 'react';
import '@/styles/button/button.module.css';

type ButtonType = 'button-primary' | 'button-secondary';
type ButtonSize = 'button-full';

interface ButtonProps {
    title: string;
    type?: ButtonType;
    action?: (event: MouseEvent<HTMLButtonElement>) => void;
    size?: ButtonSize;
}

const Button: FC<ButtonProps> = ({
                                     title,
                                     type = 'button-primary',
                                     action,
                                     size = ''
                                 }) => {
    return (
        <div>
            <button
                className={`button ${type} ${size}`}
                onClick={action}
                type="button"
            >
                {title}
            </button>
        </div>
    );
};

export default Button;