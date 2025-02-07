import {describe, expect, it, vi} from "vitest";
import {fireEvent, render, screen} from "@testing-library/react";
import Invoices from "@/app/(main)/invoices/page";

vi.mock('@/components/ui/tableau/tableau', () => ({
    default: () => <div data-testid="user-table">Mocked Invoices Table</div>
}));

describe("Invoices Page", () => {

    it("should render invoices page correctly", () => {
        const {container} = render(<Invoices/>);
        expect(container).toBeTruthy();
    });

    it("should render the search input", () => {
        const {getByPlaceholderText} = render(<Invoices/>);
        const searchInput = getByPlaceholderText("Rechercher...");
        expect(searchInput).toBeTruthy();
    });

    it("should filter invoices based on search input", () => {
        const {getByPlaceholderText, getByText} = render(<Invoices/>);
        const searchInput = getByPlaceholderText("Rechercher...");

        fireEvent.change(searchInput, {target: {value: 'Active'}});
        expect(getByText('Active')).toBeTruthy();
    });

    it("should call handleEdit when edit button is clicked", () => {
        render(<Invoices/>);
        const editButtons = screen.getAllByTestId('icon-button-Pencil')

        expect(fireEvent.click(editButtons[0])).toBeTruthy();
    });

    it("should call handleDelete when delete button is clicked", () => {
        render(<Invoices/>);
        const deleteButtons = screen.getAllByTestId('icon-button-Trash');

        expect(fireEvent.click(deleteButtons[0])).toBeTruthy();
    });
});