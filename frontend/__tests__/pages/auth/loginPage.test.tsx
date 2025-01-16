import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import Login from "@/app/(auth)/login/page";

describe("Login Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render login page", () => {
        render(<Login />);
        expect(screen.getByText('Login')).toBeDefined();
    })
});