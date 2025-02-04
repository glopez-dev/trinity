import type {NextRequest} from 'next/server'
import {NextResponse} from 'next/server'

const publicRoutes = ['/login', '/register', '/'];

const protectedRoutes = [
    '/dashboard',
    '/invoices',
    '/products',
    '/users/customers',
    '/users/employees',
    '/reports',
    '/'
];

export function middleware(request: NextRequest) {
    const token = request.cookies.get('auth_token');
    const {pathname} = request.nextUrl;

    if (token && publicRoutes.includes(pathname)) {
        return NextResponse.redirect(new URL('/dashboard', request.url));
    }

    if (!token && protectedRoutes.includes(pathname)) {
        return NextResponse.redirect(new URL('/login', request.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: [
        ...protectedRoutes,
        ...publicRoutes,
        '/((?!api|_next/static|_next/image|favicon.ico).*)',
    ]
}