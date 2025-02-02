import {beforeAll, vi} from 'vitest';

// Mocking next/navigation module to provide useRouter error (find on https://github.com/vercel/next.js/discussions/63100#discussioncomment-8737391 )
beforeAll(() => {
    vi.mock('next/navigation', async (importOriginal) => {
        const actual = await importOriginal<typeof import('next/navigation')>();
        const { useRouter } = await vi.importActual<typeof import('next-router-mock')>('next-router-mock');
        const usePathname = vi.fn().mockImplementation(() => {
            const router = useRouter();
            return router.pathname;
        });
        const useSearchParams = vi.fn().mockImplementation(() => {
            const router = useRouter();
            return new URLSearchParams(router.query?.toString());
        });
        return {
            ...actual,
            useRouter: vi.fn().mockImplementation(useRouter),
            usePathname,
            useSearchParams,
        };
    });

    global.ResizeObserver = vi.fn().mockImplementation(() => ({
        observe: vi.fn(),
        unobserve: vi.fn(),
        disconnect: vi.fn(),
    }))
});