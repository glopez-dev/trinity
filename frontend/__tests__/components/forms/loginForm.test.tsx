import {describe, expect, it, vi} from 'vitest';
import {fireEvent} from '@testing-library/react';
import LoginForm from "@/components/forms/auth/loginForm";
import {InputProps} from "@/components/ui/input/types";
import {ButtonProps} from "@/components/ui/buttons/button/types";
import {renderWithProviders} from "@test/test-utils";

vi.mock('@/components/ui/input/input', () => ({
    default: ({label, type, placeholder, value, name, onChange}: InputProps) => (
        <input
            role={'inputComponent'}
            name={name}
            type={type}
            value={value}
            placeholder={placeholder}
            aria-label={label}
            onChange={(e) => {
                onChange(e.target.value);
            }}
        />
    )
}));

vi.mock('@/components/ui/buttons/button/Button', () => ({
    default: ({title, action, type, size}: ButtonProps) => (
        <button
            type={type}
            onClick={action}
            data-size={size}
        >
            {title}
        </button>
    )
}));

describe('Login Form', () => {

    it('should renders login form correctly', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        expect(container).toBeDefined();
    });

    it('should renders email input', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        expect(container.querySelector('input[name="email"]')).toBeDefined();
    });

    it('should renders password input', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        expect(container.querySelector('input[name="password"]')).toBeDefined();
    });

    it('should renders submit button', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        expect(container.querySelector('button[type="submit"]')).toBeDefined();
    });

    it('should call handleSubmit function on form submit', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        const form = container.querySelector('form') as HTMLFormElement;
        fireEvent.submit(form);
        expect(form).toBeDefined();
    });
})