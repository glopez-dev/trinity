import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import ProductUpdatePage from "@/app/(main)/products/[id]/update/page";

describe("Product Detail Page", () => {
    it("should render product detail page", () => {
        render(<ProductUpdatePage />);
        expect(screen.getByText("Product Update Page")).toBeDefined();
    })
});