import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import ProductDetailPage from "@/app/(main)/products/[id]/page";

describe("Product Detail Page", () => {
    it("should render product detail page", () => {
        render(<ProductDetailPage/>);
        expect(screen.getByText("Product Detail Page")).toBeDefined();
    })
});