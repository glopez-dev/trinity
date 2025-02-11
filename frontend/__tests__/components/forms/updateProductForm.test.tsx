import {beforeEach, describe, expect, it, vi} from 'vitest';
import {fireEvent, screen} from '@testing-library/react';
import UpdateProductForm from '@/components/forms/products/updateProductForm';
import {renderWithProviders} from "@test/test-utils";
import {ProductResponse} from "@/lib/types/product/product";
import {ButtonProps} from "@/components/ui/buttons/button/types";
import {InputProps} from "@/components/ui/input/types";

const mockProduct: ProductResponse = {
    "id": "1",
    "barcode": "123456789",
    "category": "Beverages",
    "brand": "MockBrand",
    "name": "MockProduct",
    "ingredients": "Mock ingredients",
    "price": 10.99,
    "lastUpdate": new Date().toISOString(),
    "stock": {
        "quantity": 100,
        "minThreshold": 10,
        "maxThreshold": 200
    },
    "nutrientLevels": {
        "fat": "low",
        "saturatedFat": "low",
        "sugars": "moderate",
        "salt": "low"
    },
    "nutriments": {
        "energyKcal100g": 100,
        "proteins100g": 10,
        "carbohydrates100g": 20,
        "fat100g": 5,
        "fiber100g": 3,
        "salt100g": 0.5,
        "sugars100g": 15
    },
    "nutriscoreGrade": "b",
    "selectedImages": {
        "display": {
            "en": "",
            "fr": ""
        },
        "small": {
            "en": "",
            "fr": ""
        },
        "thumb": {
            "en": "",
            "fr": ""
        }
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
});