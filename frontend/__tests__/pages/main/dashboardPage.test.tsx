import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import Dashboard from "@/app/(main)/dashboard/page";

describe("Dashboard Page", () => {

    it("should render dashboard page", () => {
        render(<Dashboard/>);
        expect(screen.getByText("Dashboard")).toBeDefined();

    })
});