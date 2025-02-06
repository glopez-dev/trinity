import {beforeEach, describe, expect, it, vi} from 'vitest';
import {fireEvent, render, screen} from '@testing-library/react';
import DateRangePicker from '@/components/ui/buttons/day-picker/DayRangePicker';
import {endOfMonth, format, startOfMonth} from 'date-fns';
import {fr} from 'date-fns/locale';

vi.mock('lucide-react', () => ({
    Calendar: () => <div data-testid="calendar-icon">Calendar Icon</div>
}));

describe('DateRangePicker', () => {
    const defaultMonth = new Date();
    const defaultFrom = startOfMonth(defaultMonth);
    const defaultTo = endOfMonth(defaultMonth);

    beforeEach(() => {
        vi.clearAllMocks();
        console.log = vi.fn();
    });

    it('renders the component with default values', () => {
        render(<DateRangePicker/>);

        expect(screen.getByTestId('calendar-icon')).toBeDefined();

        const expectedDateText = `${format(defaultFrom, 'MMM. dd yyyy', {locale: fr})} - ${format(defaultTo, 'MMM. dd yyyy', {locale: fr})}`;
        expect(screen.getByText(expectedDateText)).toBeDefined();
    });

    it('opens the calendar dropdown when clicking the button', async () => {
        const {container} = render(<DateRangePicker/>);

        const button = screen.getByRole('button');
        fireEvent.click(button);

        expect(container.querySelector('.calendarDropdown')).toBeDefined();
    });

    it('closes the calendar dropdown on second click', async () => {
        const {container} = render(<DateRangePicker/>);

        const button = screen.getByRole('button');
        fireEvent.click(button);
        expect(container.querySelector('.calendarDropdown')).toBeDefined();

        fireEvent.click(button);
        expect(container.querySelector('.calendarDropdown')).toBeNull();

    });

    it('updates the date range when selecting new dates', async () => {
        render(<DateRangePicker/>);

        fireEvent.click(screen.getByRole('button'));

        const dayButtons = screen.getAllByRole('button', {name: /\d+/});

        fireEvent.click(dayButtons[1]);
        fireEvent.click(dayButtons[6]);

        const selectedStartDate = new Date(defaultMonth);
        selectedStartDate.setDate(parseInt(dayButtons[1].textContent || '1'));
        const selectedEndDate = new Date(defaultMonth);
        selectedEndDate.setDate(parseInt(dayButtons[6].textContent || '6'));

        const expectedDateText = `${format(selectedStartDate, 'MMM. dd yyyy', {locale: fr})} - ${format(selectedEndDate, 'MMM. dd yyyy', {locale: fr})}`;
        fireEvent.click(dayButtons[0]);
        expect(screen.getByText(expectedDateText)).toBeDefined();
    });
});