import {describe, expect, it, vi} from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import IconButton from '@/components/ui/buttons/icon-button/IconButton';

describe('Icon Button', () => {

    it('renders correctly with default props', () => {
        const onClick = vi.fn();
        render(<IconButton icon={'Filter'} onClick={onClick}/>);
        const button = screen.getByTestId('icon-button-Filter');
        expect(button).toBeDefined();
    });


    it('handles click events', () => {
        const handleClick = vi.fn();
        render(<IconButton icon={'Filter'} onClick={handleClick}/>);

        fireEvent.click(screen.getByTestId('icon-button-Filter'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });

});