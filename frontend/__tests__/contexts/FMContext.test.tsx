import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import {afterEach, describe, expect, it} from 'vitest';
import {FlashProvider, useFlash} from '@/lib/contexts/FlashMessagesContext';
import {MessageType} from "@/components/ui/flash-messages/types";

interface TestComponentProps {
    type: MessageType;
}

const TestComponent = ({type}: TestComponentProps) => {
    const {showMessage} = useFlash();
    return (
        <button role={`button-${type}`} onClick={() => showMessage(type, 'Test Message')}>
            Show Message
        </button>
    );
};


describe('FlashContext', () => {
    afterEach(cleanup);

    it('should render children correctly', () => {
        render(
            <FlashProvider>
                <div data-testid="child">Test Child</div>
            </FlashProvider>
        );

        expect(screen.getByTestId('child')).toBeTruthy();
    });

    it('should show and hide flash message', async () => {
        render(
            <FlashProvider>
                <TestComponent type={'success'}/>
            </FlashProvider>
        );

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();
    });

    it('should render flash message with different types', () => {
        render(
            <FlashProvider>
                <TestComponent type={'error'}/>
                <TestComponent type={'warning'}/>
                <TestComponent type={'info'}/>
                <TestComponent type={'success'}/>
            </FlashProvider>
        );

        fireEvent.click(screen.getByRole('button-success'));
        expect(screen.getByText('Test Message')).toBeDefined();

        fireEvent.click(screen.getByRole('button-error'));
        expect(screen.getByText('Test Message')).toBeDefined();

        fireEvent.click(screen.getByRole('button-warning'));
        expect(screen.getByText('Test Message')).toBeDefined();

        fireEvent.click(screen.getByRole('button-info'));
        expect(screen.getByText('Test Message')).toBeDefined();
    });

    it('should throw error when useFlash is used outside provider', () => {
        expect(() => render(<TestComponent type={'success'}/>)).toThrow('useFlash must be used within a FlashProvider');
    });

    it('should hide flash message after 3 seconds', async () => {
        render(
            <FlashProvider>
                <TestComponent type={'error'}/>
            </FlashProvider>
        );

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();

        await new Promise((r) => setTimeout(r, 3100));
        expect(screen.queryByText('Test Message')).toBeNull();
    });

    it('should hide flash message when close button is clicked', async () => {
        render(
            <FlashProvider>
                <TestComponent type={'error'}/>
            </FlashProvider>
        );

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();

        fireEvent.click(screen.getByRole('close-button'));
        expect(screen.queryByText('Test Message')).toBeNull();
    });
});