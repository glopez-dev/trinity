import {describe, expect, it, Mock, vi} from "vitest";
import {fireEvent, render, screen} from "@testing-library/react";
import Invoices from "@/app/(main)/invoices/page";
import {useRouter} from "next/navigation";

vi.mock('@/components/ui/tableau/tableau', () => ({
    default: () => <div data-testid="user-table">Mocked Invoices Table</div>
}));

vi.mock('next/navigation', () => ({
    useRouter: vi.fn(),
}));

const mockInvoices = [
    {
        id: '1',
        billingInfo: {
            firstName: 'John',
            lastName: 'Doe'
        },
        totalAmount: {
            value: 100,
            currency: 'EUR'
        },
        status: 'PAID'
    }
];


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
        const {getByPlaceholderText, getAllByText} = render(<Invoices/>);
        const searchInput = getByPlaceholderText("Rechercher...");

        fireEvent.change(searchInput, {target: {value: 'Paid'}});
        expect(getAllByText('Paid')).toBeTruthy();
    });

    it('should navigate to the correct invoice page when clicking the eye icon', async () => {
        const pushMock = vi.fn();
        (useRouter as Mock).mockReturnValue({
            push: pushMock
        });

        render(<Invoices />);

        await screen.findByRole('table');

        const links = screen.getAllByRole('link');
        fireEvent.click(links[0]);

        expect(links[0].getAttribute('href')).toEqual('/invoices/INV2-QXWN-W3VH-Q8H7-XH8J');
    });
});