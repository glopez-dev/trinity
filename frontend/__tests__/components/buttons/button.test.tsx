import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import Button from '@/components/ui/buttons/button/Button';

describe('Button', () => {

    afterEach(() => {
        cleanup();
    });

    it('should renders correctly with default props', () => {
        const handleAction = vi.fn();
        render(<Button title={'Button'} action={handleAction}/>);
        const button = screen.getByRole('button');
        expect(button).toBeDefined();
    });

    it('should applies different variants correctly', () => {
        const handleAction = vi.fn();
        const {rerender} = render(<Button
            title={'Button Primary'}
            action={handleAction}
            color={'primary'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('primary');

        rerender(<Button
            title={'Button Secondary'}
            action={handleAction}
            color={'secondary'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('secondary');

        rerender(<Button
            title={'Button Accent'}
            action={handleAction}
            color={'accent'}
        />);
        expect(screen.getByRole('button').getAttribute('class')).toContain('accent');
    });


    it('should applies fullWidth class when specified', () => {
        render(<Button title={'Button Full'} size="full"/>);
        expect(screen.getByRole('button').getAttribute('class')).toContain('full');
    });

    it('should handles click events', () => {
        const handleClick = vi.fn();
        render(<Button title={'Click me'} action={handleClick}/>);

        fireEvent.click(screen.getByRole('button'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });

    it('should renders an icon when specified', () => {
        render(<Button title={'Button with Icon'} icon={'Plus'}/>);
        expect(screen.getByRole('button').querySelector('svg')).toBeDefined();
    });

    it('should renders button different types', () => {
        const {rerender} = render(<Button title={'Button Submit'} type={'submit'}/>);
        expect(screen.getByRole('button').getAttribute('type')).toBe('submit');

        rerender(<Button title={'Button Reset'} type={'reset'}/>);
        expect(screen.getByRole('button').getAttribute('type')).toBe('reset');

        rerender(<Button title={'Button Button'}/>);
        expect(screen.getByRole('button').getAttribute('type')).toBe('button');
    });
});