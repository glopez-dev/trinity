import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import AddUser from '@/components/forms/users/formUser';
import {InputProps} from "@/components/ui/input/types";
import {ButtonProps} from "@/components/ui/buttons/button/types";


vi.mock('@/components/ui/input/input', () => ({
    default: ({label, type, placeholder, value, name, onChange}: InputProps) => (
        <input
            name={name}
            type={type}
            value={value}
            placeholder={placeholder}
            aria-label={label}
            role={'inputComponent'}
            onChange={(e) => {
                onChange(e.target.value);
            }}
        />
    )
}));


vi.mock('@/components/ui/buttons/button/Button', () => ({
    default: ({title, action, type, size}: ButtonProps) => (
        <button
            onClick={action}
            data-size={size}
            type={type}
        >
            {title}
        </button>
    )
}));

const getInputs = (inputs: HTMLInputElement[]) => {
    let firstNameInput, lastNameInput, emailInput;

    for (const input of inputs) {
        if (input?.name === 'firstName') {
            firstNameInput = input;
        } else if (input?.name === 'lastName') {
            lastNameInput = input;
        } else if (input?.name === 'email') {
            emailInput = input;
        }
    }

    return {firstNameInput, lastNameInput, emailInput};
}


describe('getInputs function', () => {
    it('should correctly map inputs to their respective types', () => {
        const mockInputs: HTMLInputElement[] = [
            {name: 'firstName'} as HTMLInputElement,
            {name: 'lastName'} as HTMLInputElement,
            {name: 'email'} as HTMLInputElement,
        ];

        const {firstNameInput, lastNameInput, emailInput} = getInputs(mockInputs);

        expect(firstNameInput).toBe(mockInputs[0]);
        expect(lastNameInput).toBe(mockInputs[1]);
        expect(emailInput).toBe(mockInputs[2]);
    });

    it('should handle missing inputs', () => {
        const mockInputs: HTMLInputElement[] = [
            {name: 'firstName'} as HTMLInputElement,
            {name: 'email'} as HTMLInputElement,
        ];

        const result = getInputs(mockInputs);
        expect(!result.firstNameInput || !result.lastNameInput || !result.emailInput).toBe(true);
    });

    it('should handle empty array', () => {
        const {firstNameInput, lastNameInput, emailInput} = getInputs([]);

        expect(firstNameInput).toBeUndefined();
        expect(lastNameInput).toBeUndefined();
        expect(emailInput).toBeUndefined();
    });

    it('should handle inputs in different order', () => {
        const mockInputs: HTMLInputElement[] = [
            {name: 'email'} as HTMLInputElement,
            {name: 'firstName'} as HTMLInputElement,
            {name: 'lastName'} as HTMLInputElement,
        ];

        const {firstNameInput, lastNameInput, emailInput} = getInputs(mockInputs);

        expect(firstNameInput).toBe(mockInputs[1]);
        expect(lastNameInput).toBe(mockInputs[2]);
        expect(emailInput).toBe(mockInputs[0]);
    });
});

describe('AddUser Component', () => {

    afterEach(() => {
        vi.clearAllMocks();
        cleanup();
    })

    it('should render all form inputs and button', () => {
        const onSubmit = vi.fn();
        render(<AddUser onSubmit={onSubmit}/>);

        const inputs = screen.getAllByRole<HTMLInputElement>('inputComponent');
        const {firstNameInput, lastNameInput, emailInput} = getInputs(inputs);

        expect(firstNameInput).toBeDefined();
        expect(lastNameInput).toBeDefined();
        expect(emailInput).toBeDefined();
        expect(screen.getByText('Ajouter')).toBeDefined();
    });

    it('should update input values when typing', () => {
        const onSubmit = vi.fn();
        render(<AddUser onSubmit={onSubmit}/>);

        const inputs = screen.getAllByRole<HTMLInputElement>('inputComponent');
        const {firstNameInput, lastNameInput, emailInput} = getInputs(inputs);

        if (!firstNameInput || !lastNameInput || !emailInput) {
            throw new Error('Inputs not found');
        }

        fireEvent.change(firstNameInput, {target: {value: 'John'}});
        fireEvent.change(lastNameInput, {target: {value: 'Doe'}});
        fireEvent.change(emailInput, {target: {value: 'john.doe@example.com'}});
        console.log(firstNameInput?.getAttribute('value'));
        expect(firstNameInput?.getAttribute('value')).toBe('John');
        expect(lastNameInput?.getAttribute('value')).toBe('Doe');
        expect(emailInput?.getAttribute('value')).toBe('john.doe@example.com');
    });

    it('should call onSubmit with form data when button is clicked', () => {
        const onSubmit = vi.fn();
        render(<AddUser onSubmit={onSubmit}/>);

        const inputs = screen.getAllByRole<HTMLInputElement>('inputComponent');
        const {firstNameInput, lastNameInput, emailInput} = getInputs(inputs);
        if (!firstNameInput || !lastNameInput || !emailInput) {
            throw new Error('Inputs not found');
        }

        const submitButton = screen.getByText('Ajouter');

        fireEvent.change(firstNameInput, {target: {value: 'John'}});
        fireEvent.change(lastNameInput, {target: {value: 'Doe'}});
        fireEvent.change(emailInput, {target: {value: 'john.doe@example.com'}});
        fireEvent.click(submitButton);

        expect(onSubmit).toHaveBeenCalledTimes(1);
        expect(onSubmit).toHaveBeenCalledWith({
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@example.com'
        });
    });

    it('should submit form with empty values when no input is provided', () => {
        const onSubmit = vi.fn();
        render(<AddUser onSubmit={onSubmit}/>);

        const submitButton = screen.getByText('Ajouter');
        fireEvent.click(submitButton);

        expect(onSubmit).toHaveBeenCalledWith({
            firstName: '',
            lastName: '',
            email: ''
        });
    });

    it('should have correct input types', () => {
        const onSubmit = vi.fn();
        render(<AddUser onSubmit={onSubmit}/>);

        const inputs = screen.getAllByRole<HTMLInputElement>('inputComponent');
        const {firstNameInput, lastNameInput, emailInput} = getInputs(inputs);
        if (!firstNameInput || !lastNameInput || !emailInput) {
            throw new Error('Inputs not found');
        }

        expect(firstNameInput.getAttribute('type')).toBe('text');
        expect(lastNameInput.getAttribute('type')).toBe('text');
        expect(emailInput.getAttribute('type')).toBe('email');
    });
});