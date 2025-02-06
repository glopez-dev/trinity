import {describe, expect, it, vi} from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import SearchableSelect from "@/components/ui/input/select/SearchableSelect";

describe('SearchableSelect', () => {

    const mockOptions = [
        {value: '1', label: 'Option 1'},
        {value: '2', label: 'Option 2'}
    ];

    it('renders correctly with default props', () => {
        render(<SearchableSelect options={[]}/>);
        const input = screen.getByRole('textbox');
        expect(input).toBeDefined();
    });

    it('displays options when input is focused', () => {
        render(<SearchableSelect options={mockOptions}/>);
        const input = screen.getByRole('textbox');
        fireEvent.change(input, {target: {value: 'Option'}});
        expect(screen.getByText('Option 1')).toBeDefined();
        expect(screen.getByText('Option 2')).toBeDefined();
    });

    it('calls onSelect when an option is selected', () => {
        const handleSelect = vi.fn();
        render(<SearchableSelect options={mockOptions}
                                 onSelect={handleSelect}/>);
        const input = screen.getByRole('textbox');
        fireEvent.change(input, {target: {value: 'Option'}});
        fireEvent.click(screen.getByText('Option 1'));
        expect(handleSelect).toHaveBeenCalledTimes(1);
    });

    it('displays placeholder when input is empty', () => {
        render(<SearchableSelect options={mockOptions}
                                 placeholder={'Rechercher...'}/>);
        const input = screen.getByRole('textbox');
        expect(input.getAttribute('placeholder')).toBe('Rechercher...');
    });

    it('should close search when mouseclicked outside', () => {
        render(<SearchableSelect options={mockOptions}/>);
        const input = screen.getByRole('textbox');

        fireEvent.change(input, {target: {value: 'Option'}});
        expect(screen.getByText('Option 1')).toBeDefined();
        expect(screen.getByText('Option 2')).toBeDefined();

        fireEvent.mouseDown(document.body);
        expect(screen.queryByText('Option 1')).toBeNull();
        expect(screen.queryByText('Option 2')).toBeNull();
    });

    it('should render option on click on input', () => {
        const handleSelect = vi.fn();
        render(<SearchableSelect options={mockOptions} onSelect={handleSelect}/>);
        const input = screen.getByRole('textbox');
        expect(input).toBeDefined();

        fireEvent.click(input!);

        expect(screen.getByText('Option 1')).toBeDefined();
        expect(screen.getByText('Option 2')).toBeDefined();

        fireEvent.click(screen.getByText('Option 1'));

        const selectSpy = vi.spyOn(HTMLInputElement.prototype, 'select');
        fireEvent.click(input!);
        expect(selectSpy).toHaveBeenCalled();
    });
});