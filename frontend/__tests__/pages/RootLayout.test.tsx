import {afterEach, describe, expect, it} from 'vitest'
import {cleanup, render, screen} from '@testing-library/react'
import RootLayout from "@/app/layout";

describe('AuthLayout', () => {
    afterEach(() => {
        cleanup();
    });

    it('devrait rendre le contenu enfant', () => {
        render(
            <RootLayout>
                <div data-testid="child-content">Test content</div>
            </RootLayout>
        )
        expect(screen.getByTestId('child-content')).toBeDefined()
        expect(screen.getByText('Test content')).toBeDefined()
    })
})