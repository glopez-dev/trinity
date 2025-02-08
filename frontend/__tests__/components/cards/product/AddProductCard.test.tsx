import {render, screen, fireEvent} from '@testing-library/react';
import {describe, it, expect, vi} from "vitest";
import AddProductCard from '@/components/ui/cards/product/AddProductCard';
import { ProductOFF } from '@/lib/types/product/product';

const mockProduct: ProductOFF = {
    id: '1',
    barcode: '123456789',
    category: 'Beverages',
    brand: 'MockBrand',
    name: 'MockProduct',
    ingredients: 'Water, Sugar, Flavor',
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
        display: { en: '', fr: 'images/web-icon-trinity.svg' },
        small: { en: '', fr: 'images/web-icon-trinity.svg' },
        thumb: { en: '', fr: 'images/web-icon-trinity.svg' },
    },
    lastUpdate: '2023-10-01',
    stock: {
        currentQuantity: 100,
        minThreshold: 10,
        maxThreshold: 200,
    },
};

describe('AddProductCard', () => {

    it('should renders product information correctly', () => {
        render(
            <AddProductCard
                product={mockProduct}
                isSelected={false}
                onSelect={vi.fn()}
                onMouseEnter={vi.fn()}
                onMouseLeave={vi.fn()}
            />
        );

        expect(screen.getByText('MockBrand')).toBeDefined();
        expect(screen.getByText('MockProduct')).toBeDefined();
        expect(screen.getByAltText('MockBrand')).toBeDefined();
    });

    it('should calls onSelect when clicked', () => {
        const onSelectMock = vi.fn();
        render(
            <AddProductCard
                product={mockProduct}
                isSelected={false}
                onSelect={onSelectMock}
                onMouseEnter={vi.fn()}
                onMouseLeave={vi.fn()}
            />
        );

        fireEvent.click(screen.getByTestId('product-card'));
        expect(onSelectMock).toHaveBeenCalledWith(mockProduct);
    });

    it('should calls onMouseEnter and onMouseLeave on hover', () => {
        const onMouseEnterMock = vi.fn();
        const onMouseLeaveMock = vi.fn();
        render(
            <AddProductCard
                product={mockProduct}
                isSelected={false}
                onSelect={vi.fn()}
                onMouseEnter={onMouseEnterMock}
                onMouseLeave={onMouseLeaveMock}
            />
        );


        fireEvent.mouseEnter(screen.getByTestId('product-card'));
        expect(onMouseEnterMock).toHaveBeenCalledWith(mockProduct);

        fireEvent.mouseLeave(screen.getByTestId('product-card'));
        expect(onMouseLeaveMock).toHaveBeenCalled();
    });

    it('should add selectedCard class when isSelected is true', () => {
        render(
            <AddProductCard
                product={mockProduct}
                isSelected={true}
                onSelect={vi.fn()}
                onMouseEnter={vi.fn()}
                onMouseLeave={vi.fn()}
            />
        );

        expect(screen.getByTestId('product-card').getAttribute('class')).toContain('selectedCard');
    });

    it('should set the correct src and alt attributes for the Image component', () => {
        render(
            <AddProductCard
                product={mockProduct}
                isSelected={false}
                onSelect={vi.fn()}
                onMouseEnter={vi.fn()}
                onMouseLeave={vi.fn()}
            />
        );

        const image = screen.getByAltText('MockBrand') as HTMLImageElement;
        expect(image.src).toContain('images/web-icon-trinity.svg');
        expect(image.alt).toBe('MockBrand');
    });
});