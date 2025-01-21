import {z} from 'zod';

export const productSearchSchema = z.object({
    name: z.string()
});

export type ProductSearch = z.infer<typeof productSearchSchema>;