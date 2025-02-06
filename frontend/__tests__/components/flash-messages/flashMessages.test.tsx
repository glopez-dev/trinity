import {describe, expect, it, vitest} from "vitest";
import {render, screen} from "@testing-library/react";
import {FlashMessage} from "@/components/ui/flash-messages/FlashMessage";

describe('FlashMessages', () => {

    it('renders correctly with default props', () => {
        render(<FlashMessage message={'Success Message'} type={'success'}/>)
        const flashMessage = screen.getByRole('alert');
        expect(flashMessage).toBeDefined();
    });

    it('renders correclty with different types', () => {
        const {rerender} = render(
            <FlashMessage message={'Error Message'} type={'error'}/>
        );
        let flashMessage = screen.getByRole('alert');
        expect(flashMessage.getAttribute('class')).toContain('error');

        rerender(<FlashMessage message={'Warning Message'} type={'warning'}/>);
        flashMessage = screen.getByRole('alert');
        expect(flashMessage.getAttribute('class')).toContain('warning');

        rerender(<FlashMessage message={'Info Message'} type={'info'}/>);
        flashMessage = screen.getByRole('alert');
        expect(flashMessage.getAttribute('class')).toContain('info');

        rerender(<FlashMessage message={'Success Message'} type={'success'}/>);
        flashMessage = screen.getByRole('alert');
        expect(flashMessage.getAttribute('class')).toContain('success');
    })


    it('renders the correct icon for each type', () => {
        const {rerender} = render(
            <FlashMessage message={'Error Message'} type={'error'}/>
        );
        let flashMessage = screen.getByRole('alert');
        expect(flashMessage.querySelector('svg')?.getAttribute('class')).toContain('lucide-circle-x');

        rerender(<FlashMessage message={'Warning Message'} type={'warning'}/>);
        flashMessage = screen.getByRole('alert');
        expect(flashMessage.querySelector('svg')?.getAttribute('class')).toContain('lucide-triangle-alert');

        rerender(<FlashMessage message={'Info Message'} type={'info'}/>);
        flashMessage = screen.getByRole('alert');
        expect(flashMessage.querySelector('svg')?.getAttribute('class')).toContain('lucide-info');

        rerender(<FlashMessage message={'Success Message'} type={'success'}/>);
        flashMessage = screen.getByRole('alert');
        expect(flashMessage.querySelector('svg')?.getAttribute('class')).toContain('lucide-circle-check-big');
    });

    it('calls the onClose callback when the close button is clicked', () => {
        const onClose = vitest.fn();
        render(<FlashMessage message={'Success Message'} type={'success'} onClose={onClose}/>);
        const closeButton = screen.getByRole('close-button');
        closeButton.click();
        expect(onClose).toHaveBeenCalledTimes(1);
    });

    it('closes the flash message after the duration', () => {
        vitest.useFakeTimers();
        const onClose = vitest.fn();
        render(<FlashMessage message={'Success Message'} type={'success'} duration={5000} onClose={onClose}/>);
        vitest.advanceTimersByTime(5000);
        expect(onClose).toHaveBeenCalledTimes(1);
    });
});