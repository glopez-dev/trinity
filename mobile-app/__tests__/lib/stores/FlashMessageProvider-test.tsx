import React from 'react';
import {render, act, fireEvent} from '@testing-library/react-native';
import { Text, TouchableOpacity } from 'react-native';
import { FlashMessagesProvider } from '@/lib/stores/flashMessage/FlashMessageProvider';
import { useFlashMessage } from '@/lib/stores/flashMessage/useFlashStore';


const TestComponent = () => {
    const flash = useFlashMessage();

    return (
        <>
            <TouchableOpacity testID="show-success" onPress={() => flash.success('Success message')}>
                <Text>Show Success</Text>
            </TouchableOpacity>
            <TouchableOpacity testID="show-error" onPress={() => flash.error('Error message')}>
                <Text>Show Error</Text>
            </TouchableOpacity>
        </>
    );
};

describe('FlashMessagesProvider', () => {
    test('should render flash messages when triggered', () => {
        const { getByTestId, queryByText, getByText } = render(
            <FlashMessagesProvider>
                <TestComponent />
            </FlashMessagesProvider>
        );

        expect(queryByText('Success message')).toBeNull();

        act(() => {
            fireEvent.press(getByTestId('show-success'));
        });

        expect(getByText('Success message')).toBeTruthy();
    });
});