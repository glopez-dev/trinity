import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import tsconfigPaths from 'vite-tsconfig-paths'

export default defineConfig({
    plugins: [tsconfigPaths(), react()],
    test: {
        environment: 'jsdom',
        setupFiles: ['vitestSetup.ts'],
        reporters: ['default', 'junit'],
        outputFile: {
            junit: './reports/junit.xml'
        },
        coverage: {
            provider: 'v8',
            reporter: ['text', 'cobertura'],
            reportsDirectory: './reports/coverage',
            reportOnFailure: true,
            thresholds: {
                lines: 80,
                branches: 80,
                functions: 80,
                statements: 80
            },
            clean: true,
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
                'src/components/features/dashboard/**Chart.tsx',
            ]
        },
        testTimeout: 10000,
    },
})