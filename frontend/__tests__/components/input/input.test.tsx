import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import Input from '@/components/ui/input/input';
import {useState} from "react";
import {z} from "zod";
import {InputValueTypes} from "@/components/ui/input/types";


const testEmailSchema = z.string().email({message: 'Le format est invalide.'});

describe('Input', () => {
    afterEach(() => {
        cleanup();
    });

    const TestComponent = () => {
        const [testValue, setTestValue] = useState<InputValueTypes>('');
        return (
            <Input
                name={'email'}
                label={'Email'}
                type={'email'}
                value={testValue}
                onChange={(value) => setTestValue(value)}
                schema={testEmailSchema}
            />
        );
    };

    it('should renders correctly with default props', () => {
        render(<TestComponent />);
        const input = screen.getByRole('inputComponent');
        expect(input).toBeDefined();
    });

    it('should displays error message when input does not match regex', () => {
        render(<TestComponent />);
        const input = screen.getByRole('inputComponent');
        fireEvent.change(input, {target: {value: 'test'}});
        expect(screen.getByText('Le format est invalide.')).toBeDefined();
    });

    it('should not displays error message when input is valid', () => {
        render(<TestComponent />);
        const input = screen.getByRole('inputComponent');
        fireEvent.change(input, {target: {value: 'test@gmail.com'}});
        expect(screen.queryByText('Le format est invalide.')).toBeNull();
    });

    it('should displays different types of input', () => {
        const {rerender} = render(<Input value={''} name={'email'} label={'Email'} type={'email'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('email');

        rerender(<Input name={'typeText'} value={''} label={'Text'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('text');

        rerender(<Input label={'Number'} name={'testNumber'} value={''} type={'number'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('number');

        rerender(<Input label={'Password'} name={'testPassword'} value={''} type={'password'} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('password');
    })
});