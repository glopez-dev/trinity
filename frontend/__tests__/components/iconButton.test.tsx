import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import IconButton from '@/components/ui/buttons/icon-button/IconButton';

describe('Icon Button', () => {

    afterEach(() => {
        cleanup();
    });

    it('renders correctly with default props', () => {
        render(<IconButton icon={'Filter'} onClick={() => console.log('valide')}/>);
        const button = screen.getByRole('icon-button');
        expect(button).toBeDefined();
    });


    it('handles click events', () => {
        const handleClick = vi.fn();
        render(<IconButton icon={'Filter'} onClick={handleClick}/>);

        fireEvent.click(screen.getByRole('icon-button'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });

});