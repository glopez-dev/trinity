import {describe, expect, it} from 'vitest'
import {render, screen} from '@testing-library/react'
import AuthLayout from '@/app/(auth)/layout'
import {renderWithProviders} from "@test/test-utils";

describe('AuthLayout', () => {

    it('should render auth layout correctly', () => {
        const {container} = renderWithProviders(
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