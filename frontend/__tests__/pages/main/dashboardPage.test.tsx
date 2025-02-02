import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Dashboard from "@/app/(main)/dashboard/page";

describe("Dashboard Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render dashboard page", () => {
        render(<Dashboard />);
        expect(screen.getByText("Dashboard")).toBeDefined();

    })
});