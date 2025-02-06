import {describe, expect, it} from 'vitest';
import {render, screen} from '@testing-library/react';
import {NavLogo} from '@/components/ui/navigation/NavLogo';

describe('NavLogo Component', () => {

    it('should render with base logo class when not collapsed', () => {
        render(<NavLogo isCollapsed={false}/>);
        const img = screen.getByRole('img', {name: 'Trinity Logo'});
        expect(img.getAttribute('class')).toContain('logo');
    });

    it('should render with collapsed class when collapsed', () => {
        render(<NavLogo isCollapsed={true}/>);
        const img = screen.getByRole('img', {name: 'Trinity Logo'});
        expect(img.getAttribute('class')).toContain(`logo`);
        expect(img.getAttribute('class')).toContain(`collapsed`);
    });

    it('should have correct image attributes', () => {
        render(<NavLogo isCollapsed={false}/>);
        const image = screen.getByRole('img', {name: 'Trinity Logo'});

        expect(image?.getAttribute('src')).toBe('/images/logo-svg-trinity.svg');
        expect(image?.getAttribute('alt')).toBe('Trinity Logo');
        expect(image?.getAttribute('width')).toBe('144');
        expect(image?.getAttribute('height')).toBe('32');
    });
});