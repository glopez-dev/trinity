// middleware.test.ts
import {describe, expect, it, vi} from 'vitest'
import {middleware} from '@/middleware'
import {NextRequest, NextResponse} from 'next/server'

describe('Auth Middleware', () => {
    const createRequest = (path: string, hasToken: boolean = false) => {
        const url = new URL(`http://localhost${path}`)
        const req = new NextRequest(url)

        vi.spyOn(req.cookies, 'get').mockImplementation((name) => {
            if (name === 'auth_token' && hasToken) {
                return {name: 'auth_token', value: 'valid-token'}
            }
            return undefined
        })

        return req
    }

    it('should redirect to /login when accessing /dashboard without token', () => {
        const req = createRequest('/dashboard')
        const response = middleware(req)

        expect(response).toBeInstanceOf(NextResponse)
        expect(response?.headers.get('location')).toBe('http://localhost/login')
    })

    it('should allow access to /dashboard with valid token', () => {
        const req = createRequest('/dashboard', true)
        const response = middleware(req)

        expect(response).toBeInstanceOf(NextResponse)
        expect(response?.headers.get('location')).toBeNull()
    })

    it('should redirect to /dashboard when accessing /login with token', () => {
        const req = createRequest('/login', true)
        const response = middleware(req)

        expect(response).toBeInstanceOf(NextResponse)
        expect(response?.headers.get('location')).toBe('http://localhost/dashboard')
    })

    it('should allow access to /login without token', () => {
        const req = createRequest('/login')
        const response = middleware(req)

        expect(response).toBeInstanceOf(NextResponse)
        expect(response?.headers.get('location')).toBeNull()
    })

    it('should not affect static resources', () => {
        const req = createRequest('/_next/static/chunk.js')
        const response = middleware(req)

        expect(response).toBeInstanceOf(NextResponse)
        expect(response?.headers.get('location')).toBeNull()
    })
})