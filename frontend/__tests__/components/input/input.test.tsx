import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import Input from '@/components/ui/input/input';

describe('Input', () => {
    afterEach(() => {
        cleanup();
    });

    it('renders correctly with default props', () => {
        render(<Input label={'Prénom'} />);
        const input = screen.getByRole('inputComponent');
        expect(input).toBeDefined();
    });

    it('call onChange when input value changes', () => {
        const handleChange = vi.fn();
        render(<Input label={'Email'} type={'email'} onChange={handleChange} />);
        const input = screen.getByRole('inputComponent');
        fireEvent.change(input, {target: {value: 'test@gmail.com'}});
        expect(handleChange).toHaveBeenCalledTimes(1);
    })

    it('displays error message when input is required and empty', () => {
        render(<Input label={'Email'} type={'email'} required={true} />);
        const input = screen.getByRole('inputComponent');
        fireEvent.change(input, {target: {value: 'a'}});
        fireEvent.change(input, {target: {value: ''}});
        expect(screen.getByText('Ce champ est obligatoire.')).toBeDefined();
    });

    it('displays error message when input does not match regex', () => {
        render(<Input label={'Email'} type={'email'} regex={/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/} />);
        const input = screen.getByRole('inputComponent');
        fireEvent.change(input, {target: {value: 'test'}});
        expect(screen.getByText('Le format est invalide.')).toBeDefined();
    });

    it('does not display error message when input is valid', () => {
        render(<Input label={'Email'} type={'email'} regex={/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/} />);
        const input = screen.getByRole('inputComponent');
        fireEvent.change(input, {target: {value: 'test@gmail.com'}});
        expect(screen.queryByText('Le format est invalide.')).toBeNull();
    });

    it('displays different types of input', () => {
        const {rerender} = render(<Input label={'Email'} type={'email'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('email');

        rerender(<Input label={'Text'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('text');

        rerender(<Input label={'Number'} type={'number'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('number');

        rerender(<Input label={'Password'} type={'password'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('password');
    })
});