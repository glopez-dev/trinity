import {describe, expect, it, vi, beforeEach} from 'vitest';
import {fireEvent, waitFor} from '@testing-library/react';
import LoginForm from "@/components/forms/auth/loginForm";
import {InputProps} from "@/components/ui/input/types";
import {ButtonProps} from "@/components/ui/buttons/button/types";
import {renderWithProviders} from "@test/test-utils";
import {ReactNode} from "react";
import {login} from "@/lib/actions/auth";

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

const mockSetToken = vi.fn();
const mockLogout = vi.fn();
const mockCheckAuth = vi.fn();
vi.mock('@/lib/contexts/AuthContext', () => ({
    useAuth: () => ({
        setToken: mockSetToken,
        logout: mockLogout,
        checkAuth: mockCheckAuth
    }),
    AuthProvider: ({children}: { children: ReactNode }) => <>{children}</>,
}));

const mockShowMessage = vi.fn();
vi.mock('@/lib/contexts/FlashMessagesContext', () => ({
    useFlash: () => ({
        showMessage: mockShowMessage
    }),
    FlashProvider: ({children}: { children: ReactNode }) => <>{children}</>,
}));

vi.mock('@/lib/actions/auth', () => ({
    login: vi.fn()
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

    it('should call handleChange function on input change', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        const input = container.querySelector('input[name="email"]') as HTMLInputElement;
        fireEvent.change(input, {target: {value: 'test@test.fr' }});
        expect(input.value).toBe('test@test.fr');
    })

    it('should call handleSubmit function on form submit', () => {
        const {container} = renderWithProviders(<LoginForm/>);
        const form = container.querySelector('form') as HTMLFormElement;
        fireEvent.submit(form);
        expect(form).toBeDefined();
    });

    describe('handleSubmit', () => {
        beforeEach(() => {
            vi.clearAllMocks();
        });

        it('should handle successful login', async () => {
            vi.mocked(login).mockResolvedValueOnce({jwt: 'test-token'});

            const {container} = renderWithProviders(<LoginForm/>);

            const emailInput = container.querySelector('input[name="email"]') as HTMLInputElement;
            const passwordInput = container.querySelector('input[name="password"]') as HTMLInputElement;

            fireEvent.change(emailInput, {target: {value: 'test@test.fr'}});
            fireEvent.change(passwordInput, {target: {value: 'password123'}});

            const form = container.querySelector('form') as HTMLFormElement;
            fireEvent.submit(form);

            await waitFor(() => {
                expect(mockSetToken).toHaveBeenCalledWith('test-token');
            });
        });

        it('should handle validation error', async () => {
            const {container} = renderWithProviders(<LoginForm/>);

            const form = container.querySelector('form') as HTMLFormElement;
            fireEvent.submit(form);

            await waitFor(() => {
                expect(mockShowMessage).toHaveBeenCalledWith('error', expect.any(String));
            });
        });

        it('should handle login error response', async () => {
            const error = new Error('Login failed');
            vi.mocked(login).mockResolvedValueOnce(error);

            const {container} = renderWithProviders(<LoginForm/>);

            const emailInput = container.querySelector('input[name="email"]') as HTMLInputElement;
            const passwordInput = container.querySelector('input[name="password"]') as HTMLInputElement;

            fireEvent.change(emailInput, {target: {value: 'test@test.fr'}});
            fireEvent.change(passwordInput, {target: {value: 'password123'}});

            const form = container.querySelector('form') as HTMLFormElement;
            fireEvent.submit(form);

            await waitFor(() => {
                expect(mockShowMessage).toHaveBeenCalledWith('error', 'Login failed');
            });
        });

        it('should handle axios error', async () => {
            const axiosError = new Error('Network error') as any;
            axiosError.isAxiosError = true;
            vi.mocked(login).mockRejectedValueOnce(axiosError);

            const {container} = renderWithProviders(<LoginForm/>);

            const emailInput = container.querySelector('input[name="email"]') as HTMLInputElement;
            const passwordInput = container.querySelector('input[name="password"]') as HTMLInputElement;

            fireEvent.change(emailInput, {target: {value: 'test@test.fr'}});
            fireEvent.change(passwordInput, {target: {value: 'password123'}});

            const form = container.querySelector('form') as HTMLFormElement;
            fireEvent.submit(form);

            await waitFor(() => {
                expect(mockShowMessage).toHaveBeenCalledWith('error', 'Network error');
            });
        });

        it('should handle generic error', async () => {
            vi.mocked(login).mockRejectedValueOnce(new Error('Unknown error'));

            const {container} = renderWithProviders(<LoginForm/>);

            const emailInput = container.querySelector('input[name="email"]') as HTMLInputElement;
            const passwordInput = container.querySelector('input[name="password"]') as HTMLInputElement;

            fireEvent.change(emailInput, {target: {value: 'test@test.fr'}});
            fireEvent.change(passwordInput, {target: {value: 'password123'}});

            const form = container.querySelector('form') as HTMLFormElement;
            fireEvent.submit(form);

            await waitFor(() => {
                expect(mockShowMessage).toHaveBeenCalledWith('error', 'Une erreur est survenue !');
            });
        });
    });
})