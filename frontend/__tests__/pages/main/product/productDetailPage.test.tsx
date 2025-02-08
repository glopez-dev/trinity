import {beforeEach, describe, expect, it, vi} from "vitest";
import {act, fireEvent, screen} from "@testing-library/react";
import ProductDetailPage from "@/app/(main)/products/[slug]/page";
import {Product} from "@/lib/types/product/product";
import {renderWithProviders} from "@test/test-utils";
import {ReactNode} from "react";
import {ButtonProps} from "@/components/ui/buttons/button/types";

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

vi.mock('@/components/ui/modal/Modal', () => ({
    default: vi.fn(({title, body, footer, modalVisible}) => {
        if (!modalVisible) return null;

        return (
            <div data-testid="mocked-modal">
                <div data-testid="modal-title">{title}</div>
                <div data-testid="modal-body">{body}</div>
                <div data-testid="modal-footer">{footer}</div>
            </div>
        );
    })
}));

vi.mock('@/lib/constants/products', () => ({
    LOW_STOCK: 'Bas',
    NORMAL_STOCK: 'Normal',
    VERY_LOW_STOCK: 'Insuffisant',
    STOCK_STATUS: {
        LOW_STOCK: 'Bas',
        NORMAL_STOCK: 'Normal',
        VERY_LOW_STOCK: 'Insuffisant'
    },
    getStockStatus: vi.fn((currentQuantity: number, minThreshold: number) => {
        if (currentQuantity < minThreshold) {
            return 'Insuffisant';
        }
        if (currentQuantity < minThreshold * 2) {
            return 'Bas';
        }
        return 'Normal';
    })
}));

const mockPush = vi.fn();
vi.mock('next/navigation', () => ({
    useRouter: () => ({
        push: mockPush
    })
}));

vi.mock('@/components/ui/cards/product/ProductCard', () => ({
    ProductCard: vi.fn(({product}: { product: Product }) => (
        <div data-testid="product-card">
            <div data-testid="product-name">{product.name}</div>
            <div data-testid="product-price">{product.price} €</div>
            <div data-testid="product-quantity">{product.stock.currentQuantity}</div>
            <div data-testid="product-threshold">{product.stock.minThreshold}</div>
            <div data-testid="product-category">{product.category}</div>
            <div data-testid="product-last-update">{product.lastUpdate.toLocaleDateString()}</div>
        </div>
    ))
}));

describe("Product Detail Page", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });
    const productId = "8f7e9d2c-5a4b-4c3d-b1e6-f9a8b7c6d5e4"

    it('should render the product detail page', async () => {
        const params = Promise.resolve({slug: productId});

        renderWithProviders(<ProductDetailPage params={params}/>, 'flash');

        await act(async () => {
            await params;
        });
        expect(screen.getByText('Fiche Produit')).toBeDefined();
        const productCard = screen.getByTestId('product-card');
        expect(productCard).toBeDefined();
    });

    it('should handle edit button click', async () => {
        const params = Promise.resolve({slug: productId});
        renderWithProviders(<ProductDetailPage params={params}/>, 'flash');

        await act(async () => {
            await params;
        });

        const editButton = screen.getByText('Modifier');
        fireEvent.click(editButton);

        expect(mockPush).toHaveBeenCalledWith(`/products/${productId}/update`);
    });

    it('should open delete modal when delete button is clicked', async () => {
        const params = Promise.resolve({slug: productId});
        renderWithProviders(<ProductDetailPage params={params}/>, 'flash');

        await act(async () => {
            await params;
        });

        const deleteButton = screen.getByText('Supprimer');
        fireEvent.click(deleteButton);

        expect(screen.getByTestId('mocked-modal')).toBeDefined();
        expect(screen.getByTestId('modal-title').textContent).toEqual('Supprimer ce produit');
        expect(screen.getByTestId('modal-body').textContent).toEqual('Êtes-vous sur de bien vouloir supprimer ce produit ?');
    });

    it('should close modal when cancel button is clicked', async () => {
        const params = Promise.resolve({slug: '1'});
        renderWithProviders(<ProductDetailPage params={params}/>, 'flash');

        await act(async () => {
            await params;
        });

        const deleteButton = screen.getByText('Supprimer');
        fireEvent.click(deleteButton);

        const cancelButton = screen.getByText('Annuler');
        fireEvent.click(cancelButton);

        expect(screen.queryByTestId('mocked-modal')).toBeNull();
    });

    it('should handle delete confirmation', async () => {
        const params = Promise.resolve({slug: '1'});
        renderWithProviders(<ProductDetailPage params={params}/>, 'flash');

        await act(async () => {
            await params;
        });

        let buttons = screen.getAllByText('Supprimer') as HTMLButtonElement[];

        const deleteButton = buttons[0];
        fireEvent.click(deleteButton);

        buttons = screen.getAllByText('Supprimer') as HTMLButtonElement[];
        const confirmButton = buttons[1];
        fireEvent.click(confirmButton);

        expect(mockShowMessage).toHaveBeenCalledWith('success', 'Produit supprimé avec succès');
        expect(mockPush).toHaveBeenCalledWith('/products');
    });

    it('should handle product not found', async () => {
        const params = Promise.resolve({slug: 'non-existent-id'});
        renderWithProviders(<ProductDetailPage params={params}/>, 'flash');

        await act(async () => {
            await params;
        });

        expect(screen.queryByTestId('product-card')).toBeNull();
    });
});