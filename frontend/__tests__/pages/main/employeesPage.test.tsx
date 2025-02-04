import {describe, it, expect, vi, afterEach} from 'vitest';
import {cleanup, render} from '@testing-library/react';
import Home from '@/app/(main)/users/customers/page';
import EmployeesHome from "@/app/(main)/users/employees/page";

vi.mock('@/components/ui/tableau/tableau', () => ({
    default: () => <div data-testid="user-table">Mocked Employees Table</div>
}));

describe('Employees Page', () => {

    afterEach(() => {
        cleanup();
    });

    it('should render correctly', () => {
        const {container} = render(<EmployeesHome />);
        expect(container).toBeTruthy();
    });

    it('should render with container class', () => {
        const { container } = render(<EmployeesHome />);
        expect(container.querySelector('.container')).toBeTruthy();
    });

    it('should render UserTable component', () => {
        const { getByTestId } = render(<EmployeesHome />);
        expect(getByTestId('user-table')).toBeTruthy();
    });

    it('should render UserTable inside container', () => {
        const { container, getByTestId } = render(<EmployeesHome />);
        const userTable = getByTestId('user-table');
        const containerDiv = container.querySelector('.container');
        expect(containerDiv?.contains(userTable)).toBe(true);
    });
});