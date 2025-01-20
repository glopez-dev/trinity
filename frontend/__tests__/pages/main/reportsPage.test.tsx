import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Reports from "@/app/(main)/reports/page";

describe("Reports Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render reports page", () => {
        render(<Reports />);
        expect(screen.getByText('Reports')).toBeDefined();
    })
});