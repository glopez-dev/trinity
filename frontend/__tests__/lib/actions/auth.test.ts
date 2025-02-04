import { describe, it, expect, vi, beforeEach } from 'vitest';
import { login } from '@/lib/actions/auth';
import { api } from '@/lib/api/api';

vi.mock('@/lib/api/api', () => ({
    api: {
        post: vi.fn()
    }
}));

describe('login', () => {
    const mockFormData = {
        email: 'test@example.com',
        password: 'password123'
    };

    const mockSuccessResponse = {
        status: 200,
        data: {
            token: 'fake-token-123',
            user: {
                id: 1,
                email: 'test@example.com'
            }
        }
    };

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('devrait retourner les données de connexion en cas de succès', async () => {
        vi.mocked(api.post).mockResolvedValueOnce(mockSuccessResponse);

        const result = await login(mockFormData);

        expect(api.post).toHaveBeenCalledWith('/auth/login', mockFormData);
        expect(result).toEqual(mockSuccessResponse.data);
    });

    it('devrait lancer une erreur si le status n\'est pas 200', async () => {
        vi.mocked(api.post).mockResolvedValueOnce({
            ...mockSuccessResponse,
            status: 400
        });

        await expect(login(mockFormData)).rejects.toThrow('Une erreur est survenue !');
    });

    it('devrait gérer les erreurs Axios', async () => {
        const axiosError = new Error('Network Error') as any;
        axiosError.isAxiosError = true;
        vi.mocked(api.post).mockRejectedValueOnce(axiosError);

        await expect(login(mockFormData)).rejects.toThrow('Network Error');
    });

    it('devrait gérer les erreurs non-Axios', async () => {
        vi.mocked(api.post).mockRejectedValueOnce(new Error('Erreur inconnue'));

        await expect(login(mockFormData)).rejects.toThrow('Une erreur est survenue !');
    });
});