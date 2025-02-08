import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import EmployeeDetailPage from "@/app/(main)/users/employees/[id]/page";

describe("Employee Detail Page", () => {
    it("should render employee detail page", () => {
        render(<EmployeeDetailPage/>);
        expect(screen.getByText('Employee Detail Page')).toBeDefined();
    })
});