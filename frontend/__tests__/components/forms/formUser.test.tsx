import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import AddUser from '@/components/forms/users/formUser';
import {InputProps} from "@/components/ui/input/types";


vi.mock('@/components/ui/input/input', () => ({
    default: ({label, type, placeholder, value, onChange, name}: InputProps) => (
        <input
            role="inputComponent"
            name={name}
            type={type}
            value={value}
            placeholder={placeholder}
            onChange={onChange}
            aria-label={label}
        />
    )
}));

vi.mock('@/components/ui/buttons/button/Button', () => ({
    default: ({title, action, type, size}: any) => (
        <button
            onClick={action}
            data-type={type}
            data-size={size}
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
        const onSumbit = vi.fn();
        render(<AddUser onSubmit={onSumbit}/>);

        const inputs = screen.getAllByRole<HTMLInputElement>('inputComponent');
        const {firstNameInput, lastNameInput, emailInput} = getInputs(inputs);

        expect(firstNameInput).toBeDefined();
        expect(lastNameInput).toBeDefined();
        expect(emailInput).toBeDefined();
        expect(screen.getByText('Ajouter')).toBeDefined();
    });

    it('should update input values when typing', () => {
        const onSumbit = vi.fn();
        render(<AddUser onSubmit={onSumbit}/>);

        const inputs = screen.getAllByRole<HTMLInputElement>('inputComponent');
        const {firstNameInput, lastNameInput, emailInput} = getInputs(inputs);

        if (!firstNameInput || !lastNameInput || !emailInput) {
            throw new Error('Inputs not found');
        }

        fireEvent.change(firstNameInput, {target: {value: 'John'}});
        fireEvent.change(lastNameInput, {target: {value: 'Doe'}});
        fireEvent.change(emailInput, {target: {value: 'john.doe@example.com'}});

        expect(firstNameInput?.getAttribute('value')).toBe('John');
        expect(lastNameInput?.getAttribute('value')).toBe('Doe');
        expect(emailInput?.getAttribute('value')).toBe('john.doe@example.com');
    });

    it('should call onSubmit with form data when button is clicked', () => {
        const mockOnSubmit = vi.fn();
        render(<AddUser onSubmit={mockOnSubmit}/>);

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

        expect(mockOnSubmit).toHaveBeenCalledTimes(1);
        expect(mockOnSubmit).toHaveBeenCalledWith({
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@example.com'
        });
    });

    it('should submit form with empty values when no input is provided', () => {
        const mockOnSubmit = vi.fn();
        render(<AddUser onSubmit={mockOnSubmit}/>);

        const submitButton = screen.getByText('Ajouter');
        fireEvent.click(submitButton);

        expect(mockOnSubmit).toHaveBeenCalledWith({
            firstName: '',
            lastName: '',
            email: ''
        });
    });

    it('should have correct input types', () => {
        const mockOnSubmit = vi.fn();
        render(<AddUser onSubmit={mockOnSubmit}/>);

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