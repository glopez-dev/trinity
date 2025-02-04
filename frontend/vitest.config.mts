import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import tsconfigPaths from 'vite-tsconfig-paths'

export default defineConfig({
    plugins: [tsconfigPaths(), react()],
    test: {
        environment: 'jsdom',
        setupFiles: ['vitestSetup.ts'],
        coverage: {
            provider: 'v8',
            reporter: ['json', 'text', 'html'],
            enabled: true,
            exclude: [
                'node_modules',
                'src/**/types.ts',
                'src/**/types/**/*.ts',
                '.next',
                '**.config.*',
                'next-env.d.ts',
                'src/components/ui/tableau/**',
                'src/lib/api',
                'src/lib/schemas/**',
                '__tests__/**/__snapshots__/**',
                '__tests__/test-utils.tsx',
            ]
        },
        testTimeout: 10000,
    },
})