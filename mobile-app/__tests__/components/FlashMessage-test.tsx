import React from 'react';
import {act, fireEvent, render} from '@testing-library/react-native';
import {FlashMessage} from '@/components/ui/flashMessage/FlashMessage';


jest.useFakeTimers();

describe('FlashMessage', () => {
    test('should render with correct message and type', () => {
        const {getByText, getByTestId} = render(
            <FlashMessage
                message="Test message"
                type="success"
            />
        );

        expect(getByText('Test message')).toBeTruthy();
        expect(getByTestId('flash-message-success')).toBeTruthy();
    });

    test('should call onClose when close button is pressed', () => {
        const onCloseMock = jest.fn();
        const {getByLabelText} = render(
            <FlashMessage
                message="Test message"
                type="success"
                onClose={onCloseMock}
            />
        );

        act(() => {
            fireEvent.press(getByLabelText('Close message'));
            jest.runAllTimers();
        });

        expect(onCloseMock).toHaveBeenCalled();

    });

    test('should auto-close after specified duration', () => {

        const onCloseMock = jest.fn();
        render(
            <FlashMessage
                message="Test message"
                type="success"
                duration={1000}
                onClose={onCloseMock}
            />
        );

        act(() => {
            jest.advanceTimersByTime(1300);
        });

        expect(onCloseMock).toHaveBeenCalled();

    });
});