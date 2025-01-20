import {z} from 'zod';

export const userSchema = z.object({
  id: z.string(),
  email: z.string().email(),
  name: z.string(),
  role: z.string(),
});

export type User = z.infer<typeof userSchema>;

export const loginSchema = z.object({
    email: z.string().email({ message: 'Email invalide' }),
    password: z.string(),
});

export type Login = z.infer<typeof loginSchema>;