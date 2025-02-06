import {afterEach, beforeEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import Input from '@/components/ui/input/input';
import {useState} from "react";
import {z} from "zod";
import {InputValueTypes} from "@/components/ui/input/types";


const testEmailSchema = z.string().email({message: 'Le format est invalide.'});

describe('Input', () => {

    beforeEach(() => {
        vi.clearAllMocks();
        console.log = vi.fn();
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
        const {rerender} = render(<Input value={''} name={'email'} label={'Email'} type={'email'} onChange={(value) => console.log(value)} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('email');
        fireEvent.change(screen.getByRole('inputComponent'), {target: {value: 'test@test.test' }});
        expect(console.log).toHaveBeenCalledWith('test@test.test');


        rerender(<Input name={'typeText'} value={''} label={'Text'} onChange={(value) => console.log(value)} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('text');
        fireEvent.change(screen.getByRole('inputComponent'), {target: {value: 'test@test.test' }});
        expect(console.log).toHaveBeenCalledWith('test@test.test');

        rerender(<Input label={'Number'} name={'testNumber'} value={''} type={'number'} onChange={(value) => console.log(value)} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('number');
        fireEvent.change(screen.getByRole('inputComponent'), {target: {value: 3 }});
        expect(console.log).toHaveBeenCalledWith('3');

        rerender(<Input label={'Password'} name={'testPassword'} value={''} type={'password'} onChange={(value) => console.log(value)} />);
        expect(screen.getByRole('inputComponent').getAttribute('type')).toBe('password');
        fireEvent.change(screen.getByRole('inputComponent'), {target: {value: 'test@test.test' }});
        expect(console.log).toHaveBeenCalledWith('test@test.test');
    })
});