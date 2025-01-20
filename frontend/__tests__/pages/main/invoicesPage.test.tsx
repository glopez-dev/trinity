import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Invoices from "@/app/(main)/invoices/page";

describe("Invoices Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render invoices page", () => {
        render(<Invoices />);
        expect(screen.getByText('Invoices')).toBeDefined();
    })
});