import {afterEach, describe, expect, it} from 'vitest'
import {cleanup, render, screen} from '@testing-library/react'
import AuthLayout from '@/app/(auth)/layout'

describe('AuthLayout', () => {
    afterEach(() => {
        cleanup();
    });

    it('devrait afficher le titre "Auth Layout"', () => {
        render(
            <AuthLayout>
                <div>Test content</div>
            </AuthLayout>
        )

        expect(screen.getByRole('heading', {name: /auth layout/i})).toBeDefined()
    })

    it('devrait rendre le contenu enfant', () => {
        render(
            <AuthLayout>
                <div data-testid="child-content">Test content</div>
            </AuthLayout>
        )

        expect(screen.getByTestId('child-content')).toBeDefined()
        expect(screen.getByText('Test content')).toBeDefined()
    })
})