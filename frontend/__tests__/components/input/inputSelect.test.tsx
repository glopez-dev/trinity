import {afterEach, describe, expect, it, vi} from 'vitest';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import SearchableSelect from "@/components/ui/input/select/SearchableSelect";

describe('SearchableSelect', () => {
    afterEach(() => {
        cleanup();
    });

    it('renders correctly with default props', () => {
        render(<SearchableSelect options={[]}/>);
        const input = screen.getByRole('textbox');
        expect(input).toBeDefined();
    });

    it('displays options when input is focused', () => {
        render(<SearchableSelect options={[{value: '1', label: 'Option 1'}, {value: '2', label: 'Option 2'}]}/>);
        const input = screen.getByRole('textbox');
        fireEvent.change(input, {target: {value: 'Option'}});
        expect(screen.getByText('Option 1')).toBeDefined();
        expect(screen.getByText('Option 2')).toBeDefined();
    });

    it('calls onSelect when an option is selected', () => {
        const handleSelect = vi.fn();
        render(<SearchableSelect options={[{value: '1', label: 'Option 1'}, {value: '2', label: 'Option 2'}]}
                                 onSelect={handleSelect}/>);
        const input = screen.getByRole('textbox');
        fireEvent.change(input, {target: {value: 'Option'}});
        fireEvent.click(screen.getByText('Option 1'));
        expect(handleSelect).toHaveBeenCalledTimes(1);
    });

    it('displays placeholder when input is empty', () => {
        render(<SearchableSelect options={[{value: '1', label: 'Option 1'}, {value: '2', label: 'Option 2'}]}
                                 placeholder={'Rechercher...'}/>);
        const input = screen.getByRole('textbox');
        expect(input.getAttribute('placeholder')).toBe('Rechercher...');
    });
});