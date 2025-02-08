import {describe, expect, it, vi} from 'vitest';
import {render} from '@testing-library/react';
import CustomersHome from "@/app/(main)/users/customers/page";

vi.mock('@/components/ui/tableau/tableau', () => ({
    default: () => <div data-testid="user-table">Mocked Customers Table</div>
}));

describe('Customers Page', () => {

    it('should render correctly', () => {
        const {container} = render(<CustomersHome/>);
        expect(container).toBeTruthy();
    });

    it('should render with container class', () => {
        const {container} = render(<CustomersHome/>);
        expect(container.querySelector('.container')).toBeTruthy();
    });

    it('should render UserTable component', () => {
        const {getByTestId} = render(<CustomersHome/>);
        expect(getByTestId('user-table')).toBeTruthy();
    });

    it('should render UserTable inside container', () => {
        const {container, getByTestId} = render(<CustomersHome/>);
        const userTable = getByTestId('user-table');
        const containerDiv = container.querySelector('.container');
        expect(containerDiv?.contains(userTable)).toBe(true);
    });
});