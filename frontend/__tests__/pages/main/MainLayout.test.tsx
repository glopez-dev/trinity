import {afterEach, describe, expect, it} from 'vitest'
import {cleanup, render, screen} from '@testing-library/react'
import MainLayout from "@/app/(main)/layout";

describe('AuthLayout', () => {
    afterEach(() => {
        cleanup();
    });

    it('devrait afficher le titre "Auth Layout"', () => {
        render(
            <MainLayout>
                <div>Test content</div>
            </MainLayout>
        )
        expect(screen.getByRole('navigation')).toBeDefined()
    })

    it('devrait rendre le contenu enfant', () => {
        render(
            <MainLayout>
                <div data-testid="child-content">Test content</div>
            </MainLayout>
        )
        expect(screen.getByTestId('child-content')).toBeDefined()
        expect(screen.getByText('Test content')).toBeDefined()
    })
})