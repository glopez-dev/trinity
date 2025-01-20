import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Products from "@/app/(main)/products/page";

describe("Products Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render product page", () => {
        render(<Products />);
        expect(screen.getByText('Products')).toBeDefined();
    })
});