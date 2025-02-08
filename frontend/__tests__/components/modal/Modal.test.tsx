import React from 'react';
import { describe, it, expect, vi } from "vitest";
import {fireEvent, render, screen} from '@testing-library/react';
import Modal from '@/components/ui/modal/Modal';


describe('Modal', () => {
    const mockSetModalVisible = vi.fn(); // Utilisation de vi.fn() pour Vitest
    const defaultProps = {
        title: 'Test Modal',
        body: <div>Test Body</div>,
        footer: <div>Test Footer</div>,
        modalVisible: true,
        setModalVisible: mockSetModalVisible
    };

    it('should render the modal', () => {
        render(<Modal {...defaultProps}/>)

        expect(screen.getByText('Test Modal')).toBeDefined();
    })

    it('should not render the modal', () => {
        render(<Modal {...defaultProps} modalVisible={false}/>)

        expect(screen.queryByText('Test Modal')).not.toBe(undefined);
    })

    it('should open and close the modal', () => {
        render(<Modal {...defaultProps}/>)

        expect(screen.getByText('Test Modal')).toBeDefined();

        const closeButton = screen.getByRole('button');
        fireEvent.click(closeButton);

        expect(mockSetModalVisible).toHaveBeenCalledTimes(1);
        expect(mockSetModalVisible).toHaveBeenCalledWith(false);
    })
});