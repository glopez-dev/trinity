import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Register from "@/app/(auth)/register/page";

describe("Register Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render register page", () => {
        render(<Register />);
        expect(screen.getByText('Register')).toBeDefined();
    })
});