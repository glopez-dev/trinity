import {beforeEach, describe, expect, it, vi} from "vitest";
import {act, render, screen} from "@testing-library/react";
import EmployeeDetailPage from "@/app/(main)/users/employees/[id]/page";
import {ReactNode} from "react";
import {ButtonProps} from "@/components/ui/buttons/button/types";

const mockPush = vi.fn();
vi.mock('next/navigation', () => ({
    useRouter: () => ({
        push: mockPush
    })
}));

const mockSetToken = vi.fn();
const mockLogout = vi.fn();
const mockCheckAuth = vi.fn();
vi.mock('@/lib/contexts/AuthContext', () => ({
    useAuth: () => ({
        setToken: mockSetToken,
        logout: mockLogout,
        checkAuth: mockCheckAuth
    }),
    AuthProvider: ({children}: { children: ReactNode }) => <>{children}</>,
}));

const mockShowMessage = vi.fn();
vi.mock('@/lib/contexts/FlashMessagesContext', () => ({
    useFlash: () => ({
        showMessage: mockShowMessage
    }),
    FlashProvider: ({children}: { children: ReactNode }) => <>{children}</>,
}));

vi.mock('@/components/ui/buttons/button/Button', () => ({
    default: ({title, action, type, size}: ButtonProps) => (
        <button
            type={type}
            onClick={action}
            data-size={size}
        >
            {title}
        </button>
    )
}));

vi.mock('@/components/ui/modal/Modal', () => ({
    default: vi.fn(({title, body, footer, modalVisible}) => {
        if (!modalVisible) return null;

        return (
            <div data-testid="mocked-modal">
                <div data-testid="modal-title">{title}</div>
                <div data-testid="modal-body">{body}</div>
                <div data-testid="modal-footer">{footer}</div>
            </div>
        );
    })
}));

describe("Employee Detail Page", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });
    const employeeId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

    it("should render employee detail page", async () => {
        const params = Promise.resolve({id: employeeId});

        render(<EmployeeDetailPage params={params}/>);

        await act(async () => {
            await params;
        });
        expect(screen.getByText('Fiche Employé')).toBeDefined();
    })
});