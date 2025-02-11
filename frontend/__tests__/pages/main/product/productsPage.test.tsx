import {describe, expect, it, vi, vitest} from "vitest";
import {fireEvent, render, screen} from "@testing-library/react";
import Products from "@/app/(main)/products/page";
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


describe("Products Page", () => {

    it("should render product page", () => {
        const {container} = render(<Products/>);
        expect(container).toBeDefined();
        expect(screen.getByText('Liste des produits')).toBeDefined();
    })

    it("should render search input", () => {
        const {container} = render(<Products/>);
        expect(container).toBeDefined();
        expect(screen.getByPlaceholderText('Rechercher un produit')).toBeDefined();
    });

    it("should refresh the products list", () => {
        vitest.useFakeTimers();
        const {container} = render(<Products/>);
        expect(container).toBeDefined();
        const refreshButton = container.querySelector('.lucide-refresh-cw')?.parentElement as HTMLElement;
        expect(refreshButton).toBeDefined();

        fireEvent.click(refreshButton);
        expect(screen.getAllByRole('card-skeleton').length).eq(4);
    });
});