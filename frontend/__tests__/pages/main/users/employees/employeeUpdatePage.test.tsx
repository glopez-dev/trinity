import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import EmployeeUpdatePage from "@/app/(main)/users/employees/[id]/update/page";

describe("Employee Update Page", () => {
    it("should render employee update page", () => {
        render(<EmployeeUpdatePage/>);
        expect(screen.getByText('Employee Update Page')).toBeDefined();
    })
});