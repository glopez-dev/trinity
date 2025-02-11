import {describe, expect, it, vi} from "vitest";
import {render, screen} from "@testing-library/react";
import AddUserPage from "@/app/(main)/users/employees/create/page";
import {InputProps} from "@/components/ui/input/types";


vi.mock('@/components/ui/input/input', () => ({
    default: ({label, type, placeholder, value, name, onChange}: InputProps) => (
        <input
            name={name}
            type={type}
            value={value}
            placeholder={placeholder}
            aria-label={label}
            role={'inputComponent'}
            onChange={(e) => {
                onChange(e.target.value);
            }}
        />
    )
}));

describe("Dashboard Page", () => {

    it("should render dashboard page", () => {
        render(<AddUserPage/>);
        expect(screen.getByText('Ajouter un employé')).toBeDefined();
    })
});