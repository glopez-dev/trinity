import {describe, expect, it, vi} from 'vitest';
import {render, screen} from '@testing-library/react';
import AddProductPage from '@/app/(main)/products/add/page';

vi.mock("@/components/forms/products/productForm", () => ({
    default: vi.fn(() => <div data-testid="mocked-product-form">Product Form</div>)
}));

vi.mock('@/styles/product/addPage.module.css', () => ({
    default: {
        addProductHeader: 'mocked-header-class',
        addProductBody: 'mocked-body-class'
    }
}));

describe('AddProductPage', () => {

    it('should render without crashing', () => {
        const {container} = render(<AddProductPage/>);
        expect(container).toBeTruthy();
    });

    it('should display the "Add Product" title', () => {
        render(<AddProductPage/>);
        const heading = screen.getByText('Ajouter un produit');
        expect(heading).toBeDefined();
        expect(heading.tagName).toBe('H2');
    });

    it('should render the ProductForm component', () => {
        render(<AddProductPage/>);
        const productForm = screen.getByTestId('mocked-product-form');
        expect(productForm).toBeDefined();
    });

    it('should apply CSS classes correctly', () => {
        const {container} = render(<AddProductPage/>);

        const header = container.querySelector('.mocked-header-class');
        expect(header).toBeDefined();

        const body = container.querySelector('.mocked-body-class');
        expect(body).toBeDefined();
    });

    it('should match snapshot', () => {
        const {container} = render(<AddProductPage/>);
        expect(container).toMatchFileSnapshot('./__snapshots__/addProductPage.html');
    });
});