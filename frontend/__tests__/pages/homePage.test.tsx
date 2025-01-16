import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Home from "@/app/page";

describe("Login Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render home page", () => {
        render(<Home />);
        expect(screen.getByText('Home')).toBeDefined();
    })
});