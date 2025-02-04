import {describe, it, expect, vi, beforeEach} from "vitest";
import {act, renderHook} from "@testing-library/react";
import {AuthProvider, useAuth} from "@/lib/contexts/AuthContext";
import Cookies from "js-cookie";

vi.mock('js-cookie', () => ({
    default: {
        set: vi.fn(),
        get: vi.fn(),
        remove: vi.fn()
    }
}));

describe('AuthContext', () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should set the token in cookies', () => {
        const { result } = renderHook(() => useAuth(), {
            wrapper: AuthProvider
        });

        act(() => {
            result.current.setToken('test-jwt-token');
        });

        expect(Cookies.set).toHaveBeenCalledWith('auth_token', 'test-jwt-token', {
            expires: 30,
            secure: true,
            sameSite: 'strict'
        });
    });

    it('should remove the token on logout', () => {
        const { result } = renderHook(() => useAuth(), {
            wrapper: AuthProvider
        });

        act(() => {
            result.current.logout();
        });

        expect(Cookies.remove).toHaveBeenCalledWith('auth_token');
    });

    it('should check for the presence of the token', () => {
        vi.mocked(Cookies.get).mockReturnValueOnce({'token': 'existing-token'});

        const { result } = renderHook(() => useAuth(), {
            wrapper: AuthProvider
        });

        const isAuthenticated = result.current.checkAuth();
        expect(isAuthenticated).toBe(true);
        expect(Cookies.get).toHaveBeenCalledWith('auth_token');
    });

    it('should return false if no token is present', () => {
        vi.mocked(Cookies.get).mockReturnValueOnce(undefined);

        const { result } = renderHook(() => useAuth(), {
            wrapper: AuthProvider
        });

        const isAuthenticated = result.current.checkAuth();
        expect(isAuthenticated).toBe(false);
        expect(Cookies.get).toHaveBeenCalledWith('auth_token');
    });

    it('should throw an error if useAuth is used without Provider', () => {
        expect(() => renderHook(() => useAuth())).toThrow(
            'useAuth doit être utilisé avec un AuthProvider'
        );
    });
});