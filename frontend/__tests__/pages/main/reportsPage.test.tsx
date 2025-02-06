import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import Reports from "@/app/(main)/reports/page";

describe("Reports Page", () => {

    it("should render reports page", () => {
        render(<Reports/>);
        expect(screen.getByText('Reports')).toBeDefined();
    })
});