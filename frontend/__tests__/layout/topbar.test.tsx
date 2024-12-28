import {describe, it, expect, vi, afterEach} from 'vitest';
import {render, screen, fireEvent, cleanup} from '@testing-library/react';
import { Topbar } from '@/components/layout/navigation/components/Topbar';

describe('Topbar Component', () => {

    afterEach(() => {
        cleanup()
    });

    const renderTopbar = (isOpen = false) => {
        const toggleSpy = vi.fn();
        const utils = render(
            <Topbar isOpen={isOpen} onToggle={toggleSpy} />
        );
        return { toggleSpy, ...utils };
    };

    it('should toggle mobile menu visibility', async () => {
        const { toggleSpy } = renderTopbar();


        const menuBtn = screen.getByRole('button', {
            name: /ouvrir le menu/i
        });

        fireEvent.click(menuBtn);
        expect(toggleSpy).toHaveBeenCalledOnce();
    });

    it('should render navigation items when open', () => {
        const { rerender } = renderTopbar(false);

        expect(screen.queryByRole('navigation')).toBeNull();

        rerender(<Topbar isOpen={true} onToggle={() => {}} />);
        expect(screen.getByRole('navigation')).toBeDefined();
    });
});