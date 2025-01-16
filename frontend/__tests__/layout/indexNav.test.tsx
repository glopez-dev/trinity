import {afterEach, beforeEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import Navigation from "@/components/layout/navigation";
import {NavProps} from "@/components/layout/navigation/components/types";

vi.mock('@/components/layout/navigation/components/Sidebar', () => ({
    Sidebar: ({ isOpen, onToggle }: NavProps) => (
        <div data-testid="sidebar" data-is-open={isOpen}>
            <button onClick={onToggle}>Toggle Sidebar</button>
        </div>
    )
}));

vi.mock('@/components/layout/navigation/components/Topbar', () => ({
    Topbar: ({ isOpen, onToggle }: NavProps) => (
        <div data-testid="topbar" data-is-open={isOpen}>
            <button onClick={onToggle}>Toggle Topbar</button>
        </div>
    )
}));

describe('Navigation Component', () => {
    afterEach(() => {
        cleanup();
        vi.clearAllMocks();
    });

    const resizeWindow = (width: number) => {
        window.innerWidth = width;
        window.dispatchEvent(new Event('resize'));
    };

    beforeEach(() => {
        resizeWindow(1024);
    });

    it('should render Sidebar in desktop mode', () => {
        const { queryByTestId } = render(<Navigation />);
        expect(queryByTestId('sidebar')).toBeTruthy();
        expect(queryByTestId('topbar')).toBeFalsy();
    });

    it('should render Topbar in mobile mode', () => {
        resizeWindow(600);
        const { queryByTestId } = render(<Navigation />);
        expect(queryByTestId('topbar')).toBeTruthy();
        expect(queryByTestId('sidebar')).toBeFalsy();
    });

    it('should toggle menu state when toggle is called', () => {
        const { getByTestId } = render(<Navigation />);
        const sidebar = getByTestId('sidebar');
        const toggleButton = screen.getByText('Toggle Sidebar');

        expect(sidebar.dataset.isOpen).toBe('true'); // État initial
        fireEvent.click(toggleButton);
        expect(sidebar.dataset.isOpen).toBe('false');
    });

    it('should auto-close menu when switching to mobile', () => {
        const { rerender, getByTestId } = render(<Navigation />);

        // Vérifier l'état initial en desktop
        const sidebar = getByTestId('sidebar');
        expect(sidebar.dataset.isOpen).toBe('true');

        // Passer en mobile
        resizeWindow(600);
        rerender(<Navigation />);

        // Vérifier que le menu est fermé en mobile
        const topbar = getByTestId('topbar');
        expect(topbar.dataset.isOpen).toBe('false');
    });

    it('should maintain menu state when resizing in desktop mode', () => {
        const { rerender, getByTestId } = render(<Navigation />);

        // Changer la taille en restant en mode desktop
        resizeWindow(1200);
        rerender(<Navigation />);

        const sidebar = getByTestId('sidebar');
        expect(sidebar.dataset.isOpen).toBe('true');
    });

    it('should handle window resize event listener cleanup', () => {
        const removeEventListenerSpy = vi.spyOn(window, 'removeEventListener');
        const { unmount } = render(<Navigation />);

        unmount();

        expect(removeEventListenerSpy).toHaveBeenCalledWith('resize', expect.any(Function));
    });
});