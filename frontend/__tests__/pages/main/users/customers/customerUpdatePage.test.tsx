import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import CustomerUpdatePage from "@/app/(main)/users/customers/[id]/update/page";

describe("Customer Update Page", () => {
    it("should render customer update page", () => {
        render(<CustomerUpdatePage/>);
        expect(screen.getByText('Customer Update Page')).toBeDefined();
    })
});
