import {fireEvent, render, screen} from '@testing-library/react';
import {describe, expect, it} from 'vitest';
import {useFlash} from '@/lib/contexts/FlashMessagesContext';
import {MessageType} from "@/components/ui/flash-messages/types";
import {renderWithProviders} from "@test/test-utils";

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

    it('should render children correctly', () => {
        renderWithProviders(
            <div data-testid="child">Test Child</div>,
            'flash'
        );

        expect(screen.getByTestId('child')).toBeTruthy();
    });

    it('should show and hide flash message', async () => {
        renderWithProviders(
            <TestComponent type={'success'}/>,
            'flash'
        );

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();
    });

    it('should render flash message with different types', () => {
        renderWithProviders(
            <>
                <TestComponent type={'error'}/>
                <TestComponent type={'warning'}/>
                <TestComponent type={'info'}/>
                <TestComponent type={'success'}/>
            </>,
            'flash'
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
        expect(() => render(<TestComponent
            type={'success'}/>)).toThrow('useFlash doit être utilisé avec un FlashProvider');
    });

    it('should hide flash message after 5 seconds', async () => {
        renderWithProviders(
            <TestComponent type={'success'}/>,
            'flash'
        );

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();

        await new Promise((r) => setTimeout(r, 5100));
        expect(screen.queryByText('Test Message')).toBeNull();
    });

    it('should hide flash message when close button is clicked', async () => {
        renderWithProviders(
            <TestComponent type={'success'}/>,
            'flash'
        );

        fireEvent.click(screen.getByText('Show Message'));
        expect(screen.getByText('Test Message')).toBeDefined();

        fireEvent.click(screen.getByRole('close-button'));
        expect(screen.queryByText('Test Message')).toBeNull();
    });
});