import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import {Sidebar} from '@/components/layout/navigation/components/Sidebar';

describe('Sidebar Component', () => {

    afterEach(() => {
        cleanup()
    });

    it('should handle toggle interactions', () => {
        const toggleSpy = vi.fn();

        render(<Sidebar isOpen={true} onToggle={toggleSpy}/>);

        const toggleBtn = screen.getByRole('button', {
            name: /réduire le menu/i
        });

        fireEvent.click(toggleBtn);
        expect(toggleSpy).toHaveBeenCalledOnce();
    });

    it('should conditionally render content based on collapsed state', () => {
        const {rerender} = render(
            <Sidebar isOpen={true} onToggle={() => {
            }}/>
        );

        expect(screen.getByRole('img', {name: 'Trinity Logo'})).toBeDefined();

        expect(screen.getByRole('navigation').getAttribute('class')).toContain('navigation');

        rerender(<Sidebar isOpen={false} onToggle={() => {
        }}/>);
        expect(screen.getByRole('img', {name: 'Trinity Logo'}).getAttribute('class')).toContain('collapsed');

    });
});