import {act, renderHook} from '@testing-library/react-native';
import {useFlashMessage, useFlashStore} from '@/lib/stores/flashMessage/useFlashStore';

describe('useFlashStore', () => {
    beforeEach(() => {
        useFlashStore.setState({currentMessage: null});
    });

    test('should initialize with null currentMessage', () => {
        const {result} = renderHook(() => useFlashStore());
        expect(result.current.currentMessage).toBeNull();
    });

    test('should show a message when showMessage is called', () => {
        const {result} = renderHook(() => useFlashStore());

        act(() => {
            result.current.showMessage('success', 'Test message');
        });

        expect(result.current.currentMessage).toEqual({
            type: 'success',
            message: 'Test message'
        });
    });

    test('should clear message when hideMessage is called', () => {
        const {result} = renderHook(() => useFlashStore());

        act(() => {
            result.current.showMessage('error', 'Error message');
        });

        expect(result.current.currentMessage).not.toBeNull();

        act(() => {
            result.current.hideMessage();
        });

        expect(result.current.currentMessage).toBeNull();
    });
});

describe('useFlashMessage', () => {
    beforeEach(() => {
        useFlashStore.setState({currentMessage: null});
    });

    test('success function should show success message', () => {
        const {result} = renderHook(() => useFlashMessage());

        act(() => {
            result.current.success('Success message');
        });

        const state = useFlashStore.getState();
        expect(state.currentMessage).toEqual({
            type: 'success',
            message: 'Success message'
        });
    });

    test('error function should show error message', () => {
        const {result} = renderHook(() => useFlashMessage());

        act(() => {
            result.current.error('Error message');
        });

        const state = useFlashStore.getState();
        expect(state.currentMessage).toEqual({
            type: 'error',
            message: 'Error message'
        });
    });

    test('warning function should show warning message', () => {
        const {result} = renderHook(() => useFlashMessage());

        act(() => {
            result.current.warning('Warning message');
        });

        const state = useFlashStore.getState();
        expect(state.currentMessage).toEqual({
            type: 'warning',
            message: 'Warning message'
        });
    });

    test('info function should show info message', () => {
        const {result} = renderHook(() => useFlashMessage());

        act(() => {
            result.current.info('Info message');
        });

        const state = useFlashStore.getState();
        expect(state.currentMessage).toEqual({
            type: 'info',
            message: 'Info message'
        });
    });


});