import React, { useState } from 'react';
import { DayPicker, DateRange } from 'react-day-picker';
import {format, startOfMonth, endOfMonth} from 'date-fns';
import { fr } from 'date-fns/locale';
import { Calendar } from 'lucide-react';
import styles from './DayRangePicker.module.css';


const DateRangePicker = () => {
    const [isOpen, setIsOpen] = useState(false);
    const defaultMonth = new Date();
    const defaultSelected: DateRange = {
        from: startOfMonth(new Date()),
        to: endOfMonth(new Date())
    };
    const [range, setRange] = useState<DateRange | undefined>(defaultSelected);


    const formatRange = (range: DateRange | undefined) => {
        if (!range?.from || !range?.to) return '';
        return `${format(range.from, 'MMM. dd yyyy', { locale: fr })} - ${format(range.to, 'MMM. dd yyyy', { locale: fr })}`;
    };

    const handleOnClick = () => {
        setIsOpen(!isOpen);
        if (isOpen) {
            console.log(range);
        }
    }

    return (
        <>
            <div className={styles.datePickerContainer}>
                <button
                    className={styles.datePickerButton}
                    onClick={handleOnClick}
                    type="button"
                >
                    <Calendar size={24} />
                    <span className={styles.dateText}>{formatRange(range)}</span>
                </button>

                {isOpen && (
                    <div className={styles.calendarDropdown}>
                        <DayPicker
                            id="test"
                            mode="range"
                            defaultMonth={defaultMonth}
                            selected={range}
                            onSelect={setRange}
                        />
                    </div>
                )}
            </div>
        </>
    );
};

export default DateRangePicker;