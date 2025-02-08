import {beforeEach, describe, expect, it, vi} from 'vitest';
import {fireEvent, screen} from '@testing-library/react';
import UpdateProductForm from '@/components/forms/products/updateProductForm';
import {renderWithProviders} from "@test/test-utils";
import {Product} from "@/lib/types/product/product";
import {ButtonProps} from "@/components/ui/buttons/button/types";
import {InputProps} from "@/components/ui/input/types";

const mockProduct: Product = {
    id: '1',
    barcode: '123456789',
    category: 'Beverages',
    brand: 'MockBrand',
    name: 'MockProduct',
    price: 10.99,
    lastUpdate: new Date(),
    stock: {
        currentQuantity: 100,
        minThreshold: 10,
        maxThreshold: 200,
    },
    nutritionalInfo: {
        energy: 100,
        proteins: 10,
        carbohydrates: 20,
        fat: 5,
        fiber: 3,
        ingredients: 'Mock ingredients'
    }
};


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


describe('UpdateProductForm', () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should render the form', () => {
        renderWithProviders(<UpdateProductForm product={mockProduct}/>);
        expect(screen.getByRole('inputComponent', {name: 'Nom'})).toBeDefined();
        expect(screen.getByRole('inputComponent', {name: 'Prix'})).toBeDefined();
    });

    it('should update the name input', () => {
        renderWithProviders(<UpdateProductForm product={mockProduct}/>);
        const nameInput = screen.getByRole('inputComponent', {name: 'Nom'}) as HTMLInputElement;
        expect(nameInput.value).toBe('MockProduct');
        nameInput.value = 'NewMockProduct';
        expect(nameInput.value).toBe('NewMockProduct');
    });

    it('should update the price input', () => {
        renderWithProviders(<UpdateProductForm product={mockProduct}/>);
        const priceInput = screen.getByRole('inputComponent', {name: 'Prix'}) as HTMLInputElement;
        expect(priceInput.value).toBe('10.99');
        priceInput.value = '20.99';
        expect(priceInput.value).toBe('20.99');
    });

    it('should call handleSubmit when form is submitted', () => {
        renderWithProviders(<UpdateProductForm product={mockProduct}/>);
        const form = screen.getByTestId('form-update');
        const handleSubmit = vi.fn();
        form.onsubmit = handleSubmit;
        const submitButton = screen.getByRole('button', {name: 'Enregistrer'});
        fireEvent.click(submitButton);
        expect(handleSubmit).toHaveBeenCalled();
    });

    it('should show a success message when form is submitted', () => {
        renderWithProviders(<UpdateProductForm product={mockProduct}/>);
        const submitButton = screen.getByRole('button', {name: 'Enregistrer'});
        fireEvent.click(submitButton);
        expect(screen.getByText('Produit mis à jour avec succès')).toBeDefined();
    });
});