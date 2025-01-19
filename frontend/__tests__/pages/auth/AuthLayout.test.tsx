import {afterEach, describe, expect, it} from 'vitest'
import {cleanup, render, screen} from '@testing-library/react'
import AuthLayout from '@/app/(auth)/layout'

describe('AuthLayout', () => {
    afterEach(() => {
        cleanup();
    });

    it('should render auth layout correctly', () => {
        const {container} = render(
            <AuthLayout>
                <div>Test content</div>
            </AuthLayout>
        )

        expect(container).toBeDefined();
        expect(container.querySelector('.container')).toBeDefined();
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