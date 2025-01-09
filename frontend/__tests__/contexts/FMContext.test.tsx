import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import {afterEach, describe, expect, it} from 'vitest';
import {FlashProvider, useFlash} from '@/lib/contexts/FlashMessagesContext';

const TestComponent = () => {
    const {showMessage} = useFlash();
    return (
        <button onClick={() => showMessage('success', 'Test Message')}>
            Show Message
        </button>
    );
};

describe('FlashContext', () => {
    afterEach(cleanup);

    it('should show and hide flash message', async () => {
        render(
            <FlashProvider>
                <TestComponent/>
            </FlashProvider>
        );

        expect(screen.queryByText('Test Message')).toBeNull();

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();

        fireEvent.click(screen.getByRole('close-button'));
        expect(screen.queryByText('Test Message')).toBeNull();
    });

    it('should throw error when useFlash is used outside provider', () => {
        expect(() => render(<TestComponent/>)).toThrow('useFlash must be used within a FlashProvider');
    });
});