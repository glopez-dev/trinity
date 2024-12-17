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
            type={'button-primary'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('button-primary');

        rerender(<Button
            title={'Button Secondary'}
            action={() => console.log('Button Secondary')}
            type={'button-secondary'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('button-secondary');
    });


    it('applies fullWidth class when specified', () => {
        render(<Button title={'Button Full'} size="button-full" />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('button-full');
    });

    it('handles click events', () => {
        const handleClick = vi.fn();
        render(<Button title={'Click me'} action={handleClick} />);

        fireEvent.click(screen.getByRole('button'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });
});