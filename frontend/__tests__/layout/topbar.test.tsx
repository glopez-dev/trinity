import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import {Topbar} from '@/components/layout/navigation/components/Topbar';
import {renderWithProviders} from "@test/test-utils";

describe('Topbar Component', () => {

    afterEach(() => {
        cleanup()
    });

    const renderTopbar = (isOpen = false) => {
        const toggleSpy = vi.fn();
        const utils = renderWithProviders(
            <Topbar isOpen={isOpen} onToggle={toggleSpy}/>
        );
        return {toggleSpy, ...utils};
    };

    it('should render the topbar correctly', async () => {
        const {container} = renderTopbar();
        await expect(container).toMatchFileSnapshot('./__snapshots__/topbarTest.tsx');
    });

    it('should toggle mobile menu visibility', async () => {
        const {toggleSpy} = renderTopbar();

        const menuBtn = screen.getByRole('button', {
            name: /ouvrir le menu/i
        });

        fireEvent.click(menuBtn);
        expect(toggleSpy).toHaveBeenCalledOnce();

    });

    it('should render navigation items when open', () => {
        const onToggle = vi.fn();
        const {rerender} = renderTopbar(false);
        expect(screen.queryByRole('navigation')).toBeNull();
        rerender(<Topbar isOpen={true} onToggle={onToggle}/>);
        expect(screen.getByRole('navigation')).toBeDefined();

        const dashboardBtn = screen.getByRole('button', {name: /dashboard/i});
        fireEvent.click(dashboardBtn);
        expect(onToggle).toHaveBeenCalledOnce();
    });
});