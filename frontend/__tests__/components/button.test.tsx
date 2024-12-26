import { describe, it, expect, vi, afterEach } from 'vitest';
import {render, screen, fireEvent, cleanup} from '@testing-library/react';
import Button from '@/components/buttons/Button';
import {randomInt} from "node:crypto";

describe('Button', () => {

    afterEach(() => {
        cleanup();
    });

    it('renders correctly with default props', () => {
        render(<Button title={'Button'} action={() => console.log('valide')}/>);
        const button = screen.getByRole('button');
        expect(button).toBeDefined();
    });

    it('applies different variants correctly', () => {
        const { rerender } = render(<Button
            title={'Button Primary'}
            action={() => console.log('Button Primary')}
            type={'primary'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('primary');

        rerender(<Button
            title={'Button Secondary'}
            action={() => console.log('Button Secondary')}
            type={'secondary'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('secondary');

        rerender(<Button
            title={'Button Accent'}
            action={() => console.log('Button Accent')}
            type={'accent'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('accent');
    });


    it('applies fullWidth class when specified', () => {
        render(<Button title={'Button Full'} size="full" />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('full');
    });

    it('handles click events', () => {
        const handleClick = vi.fn();
        render(<Button title={'Click me'} action={handleClick} />);

        fireEvent.click(screen.getByRole('button'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });
});