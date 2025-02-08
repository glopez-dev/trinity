import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import InvoiceDetailPage from "@/app/(main)/invoices/[id]/page";

describe("Invoice Detail Page", () => {
    it("should render invoice detail page", () => {
        render(<InvoiceDetailPage/>);
        expect(screen.getByText("Invoice Detail Page")).toBeDefined();
    })
});