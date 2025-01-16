import {describe, it, expect, vi, afterEach} from 'vitest';
import {cleanup, render} from '@testing-library/react';
import Home from '@/app/(main)/users/page';

vi.mock('@/components/ui/tableau/tableau', () => ({
    default: () => <div data-testid="user-table">Mocked User Table</div>
}));

describe('Home Component', () => {

    afterEach(() => {
        cleanup();
    });

    it('should render correctly', () => {
        const {container} = render(<Home />);
        expect(container).toBeTruthy();
    });

    it('should render with container class', () => {
        const { container } = render(<Home />);
        expect(container.querySelector('.container')).toBeTruthy();
    });

    it('should render UserTable component', () => {
        const { getByTestId } = render(<Home />);
        expect(getByTestId('user-table')).toBeTruthy();
    });

    it('should render UserTable inside container', () => {
        const { container, getByTestId } = render(<Home />);
        const userTable = getByTestId('user-table');
        const containerDiv = container.querySelector('.container');
        expect(containerDiv?.contains(userTable)).toBe(true);
    });
});