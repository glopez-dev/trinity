import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import {Sidebar} from '@/components/layout/navigation/components/Sidebar';
import NavItem from "@/components/ui/navigation/NavItem";
import React from "react";
import {NavigationContentProps} from "@/components/layout/navigation/components/types";
import {renderWithProviders} from "@test/test-utils";

vi.mock('@/components/ui/navigation/NavLogo', () => ({
    NavLogo: ({isCollapsed}: NavigationContentProps) => (
        <img
            src="/logo.png"
            alt="Trinity Logo"
            className={isCollapsed ? 'collapsed' : ''}
        />
    )
}));

describe('Sidebar Component', () => {

    afterEach(() => {
        cleanup()
    });

    it('should render SideBar correctly', async () => {
        renderWithProviders(<Sidebar isOpen={true} onToggle={vi.fn()}/>);
        const sidebar = screen.getByRole('navigation');
        await expect(sidebar).toMatchFileSnapshot('./__snapshots__/sidebarTest.tsx');
    });

    it('should handle toggle interactions', () => {
        const toggleSpy = vi.fn();

        renderWithProviders(<Sidebar isOpen={true} onToggle={toggleSpy}/>);

        const toggleBtn = screen.getByRole('button', {
            name: /réduire le menu/i
        });

        fireEvent.click(toggleBtn);
        expect(toggleSpy).toHaveBeenCalledOnce();
    });

    it('should conditionally render content based on collapsed state', () => {
        const onToggle = vi.fn();
        const {rerender} = renderWithProviders(
            <Sidebar isOpen={true} onToggle={onToggle}/>
        );
        expect(screen.getByRole('img', {name: 'Trinity Logo'})).toBeDefined();
        expect(screen.getByRole('navigation').getAttribute('class')).toContain('navigation');

        rerender(<Sidebar isOpen={false} onToggle={onToggle}/>);
        expect(screen.getByRole('img', {name: 'Trinity Logo'}).getAttribute('class')).toContain('collapsed');
    });

    it('should handle toggle on NavItem click', () => {
        const onToggle = vi.fn();
        renderWithProviders(
            <NavItem
                icon="LogOut"
                label="Déconnexion"
                route="/"
                isCollapsed={false}
                onToggle={onToggle}
            />
        );

        const dashboardBtn = screen.getByRole('button', {name: /déconnexion/i});
        fireEvent.click(dashboardBtn);
        expect(onToggle).toHaveBeenCalledOnce();
    });

});