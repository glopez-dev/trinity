import {beforeEach, describe, expect, it, Mock, vi} from 'vitest';
import {cleanup, fireEvent, screen, waitFor} from '@testing-library/react';
import ProductForm from '@/components/forms/products/productForm';
import {createProduct, searchOFFProducts} from '@/lib/api/products/productsProvider';
import {renderWithProviders} from "@test/test-utils";
import {ProductOFF} from "@/lib/types/product/product";
import {ButtonProps} from "@/components/ui/buttons/button/types";
import {InputProps} from "@/components/ui/input/types";
import {ReactNode} from 'react';
import {api} from "@/lib/api/api";

vi.mock('@/lib/api/products/productsProvider');
vi.mock('axios');
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

const mockPush = vi.fn();
vi.mock('next/navigation', () => ({
    useRouter: () => ({
        push: mockPush
    })
}));

const mockProducts: ProductOFF[] = [
    {
        id: 1,
        barcode: '123456789',
        category: 'Beverages',
        brand: 'MockBrand',
        name: 'Test Product 1',
        ingredients: 'Test ingredients 1',
        price: null,
        nutrientLevels: {
            fat: 'low',
            saturatedFat: 'low',
            sugars: 'high',
            salt: 'low',
        },
        nutriments: {
            energyKcal100g: 50,
            proteins100g: 0,
            carbohydrates100g: 12,
            fat100g: 0,
            fiber100g: 0,
            salt100g: 0.1,
            sugars100g: 10,
        },
        nutriscoreGrade: 'c',
        selectedImages: {
            display: {en: '', fr: 'images/web-icon-trinity.svg'},
            small: {en: '', fr: 'images/web-icon-trinity.svg'},
            thumb: {en: '', fr: 'images/web-icon-trinity.svg'},
        },
        lastUpdate: '2023-10-01',
        stock: {
            currentQuantity: 100,
            minThreshold: 10,
            maxThreshold: 200,
        },
    },
    {
        id: 2,
        barcode: '123456789',
        category: 'Beverages',
        brand: 'MockBrand',
        name: 'Test Product 2',
        ingredients: 'Test ingredients 2',
        price: null,
        nutrientLevels: {
            fat: 'low',
            saturatedFat: 'low',
            sugars: 'high',
            salt: 'low',
        },
        nutriments: {
            energyKcal100g: 50,
            proteins100g: 0,
            carbohydrates100g: 12,
            fat100g: 0,
            fiber100g: 0,
            salt100g: 0.1,
            sugars100g: 10,
        },
        nutriscoreGrade: 'c',
        selectedImages: {
            display: {en: '', fr: 'images/web-icon-trinity.svg'},
            small: {en: '', fr: 'images/web-icon-trinity.svg'},
            thumb: {en: '', fr: 'images/web-icon-trinity.svg'},
        },
        lastUpdate: '2023-10-01',
        stock: {
            currentQuantity: 100,
            minThreshold: 10,
            maxThreshold: 200,
        }
    }
];

describe('ProductForm', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        (searchOFFProducts as Mock).mockResolvedValue({products: mockProducts});
        (createProduct as Mock).mockResolvedValue({products: mockProducts});
        cleanup();
    });

    it('should display correctly the product form with search input and search button', () => {
        renderWithProviders(<ProductForm/>, 'flash');

        expect(screen.getByPlaceholderText('Rechercher un produit dans le catalogue')).toBeDefined();
        expect(screen.getByTestId('icon-button-Search')).toBeDefined();
    });

    it('should show an error message if search value is empty', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchButton = screen.getByTestId('icon-button-Search');
        fireEvent.click(searchButton);

        expect(screen.getByText('Veuillez entrer un nom de produit à rechercher')).toBeDefined();
    });

    it('should search and display the result', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        const searchButton = screen.getByTestId('icon-button-Search');

        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(searchButton);

        await waitFor(() => {
            expect(screen.getByText('Selectionnez le produit à ajouter')).toBeDefined();
            expect(screen.getByText('Test Product 1')).toBeDefined();
            expect(screen.getByText('Test Product 2')).toBeDefined();
        });
    });

    it('allows selecting a product and displays the detail form', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        const searchButton = screen.getByTestId('icon-button-Search');

        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(searchButton);

        await waitFor(() => {
            expect(screen.getByText('Test Product 1')).toBeDefined();
        });

        const productCard = screen.getAllByTestId('product-card')[0];
        fireEvent.click(productCard);

        expect(productCard.getAttribute('class')).toContain('selectedCard');
        expect(screen.getByPlaceholderText('Nom du produit...')).toBeDefined();
        expect(screen.getByPlaceholderText('Prix en €...')).toBeDefined();
        expect(screen.getByPlaceholderText('Quantité...')).toBeDefined();
        expect(screen.getByPlaceholderText('Seuil minimum...')).toBeDefined();
    });

    it('handles API errors correctly', async () => {
        (searchOFFProducts as Mock).mockRejectedValue(new Error('API Error'));

        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        const searchButton = screen.getByTestId('icon-button-Search');

        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(searchButton);

        await waitFor(() => {
            expect(screen.getByText('Une erreur est survenue ! Veuillez réessayer plus tard.')).toBeDefined();
        });
    });

    it('updates form values correctly', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        const searchButton = screen.getByTestId('icon-button-Search');

        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(searchButton);

        await waitFor(() => {
            expect(screen.getByText('Test Product 1')).toBeDefined();
        });

        const productCard = screen.getAllByTestId('product-card')[0];
        fireEvent.click(productCard);

        const nameInput = screen.getByPlaceholderText('Nom du produit...');
        const priceInput = screen.getByPlaceholderText('Prix en €...');
        const quantityInput = screen.getByPlaceholderText('Quantité...');
        const thresholdInput = screen.getByPlaceholderText('Seuil minimum...');

        fireEvent.change(nameInput, {target: {value: 'Test Product 1'}});
        fireEvent.change(priceInput, {target: {value: '10.99'}});
        fireEvent.change(quantityInput, {target: {value: '5'}});
        fireEvent.change(thresholdInput, {target: {value: '2'}});

        expect(nameInput.getAttribute('value')).toEqual('Test Product 1');
        expect(priceInput.getAttribute('value')).toEqual('10.99');
        expect(quantityInput.getAttribute('value')).toEqual('5');
        expect(thresholdInput.getAttribute('value')).toEqual('2');
    });

    it('should display tooltip on hovering product', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        const searchButton = screen.getByTestId('icon-button-Search');

        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(searchButton);

        await waitFor(() => {
            expect(screen.getByText('Test Product 1')).toBeDefined();
        });

        const productCard = screen.getAllByTestId('product-card')[0];
        fireEvent.mouseEnter(productCard);
        expect(screen.getByText('Test ingredients 1')).toBeDefined();
    });

    it('should show warning when submitting without selecting a product', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        const searchButton = screen.getByTestId('icon-button-Search');

        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(searchButton);

        await waitFor(() => {
            expect(screen.getByText('Test Product 1')).toBeDefined();
        });

        const productCard = screen.getAllByTestId('product-card')[0];
        fireEvent.click(productCard);


        const submitButton = screen.getByText('Ajouter');
        fireEvent.click(submitButton);

        expect(screen.getByText('Veuillez remplir tous les champs')).toBeDefined();
    });

    it('should show warning when submitting with missing required fields', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(screen.getByTestId('icon-button-Search'));

        await waitFor(() => {
            fireEvent.click(screen.getAllByTestId('product-card')[0]);
        });

        const priceInput = screen.getByPlaceholderText('Prix en €...');
        fireEvent.change(priceInput, {target: {value: '10.99'}});

        const submitButton = screen.getByText('Ajouter');
        fireEvent.click(submitButton);

        expect(screen.getByText('Veuillez remplir tous les champs')).toBeDefined();
    });

    it('should successfully create a product when all fields are filled', async () => {
        renderWithProviders(<ProductForm/>, 'flash');

        const searchInput = screen.getByPlaceholderText('Rechercher un produit dans le catalogue');
        fireEvent.change(searchInput, {target: {value: 'test product'}});
        fireEvent.click(screen.getByTestId('icon-button-Search'));

        await waitFor(() => {
            fireEvent.click(screen.getAllByTestId('product-card')[0]);
        });

        const priceInput = screen.getByPlaceholderText('Prix en €...');
        const quantityInput = screen.getByPlaceholderText('Quantité...');
        const thresholdInput = screen.getByPlaceholderText('Seuil minimum...');

        fireEvent.change(priceInput, {target: {value: '10.99'}});
        fireEvent.change(quantityInput, {target: {value: '5'}});
        fireEvent.change(thresholdInput, {target: {value: '2'}});

        const submitButton = screen.getByText('Ajouter');
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(createProduct).toHaveBeenCalledWith(expect.objectContaining({
                name: 'Test Product 1',
                price: '10.99',
                stock: {
                    currentQuantity: '5',
                    minThreshold: '2',
                    maxThreshold: '10000'
                }
            }));
        });
    });
});