import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import CustomerDetailPage from "@/app/(main)/users/customers/[id]/page";

describe("Customer Detail Page", () => {
    it("should render customer detail page", () => {
        render(<CustomerDetailPage/>);
        expect(screen.getByText('Customer Detail Page')).toBeDefined();
    })
});