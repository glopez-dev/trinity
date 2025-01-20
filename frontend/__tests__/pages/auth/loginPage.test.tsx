import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render} from "@testing-library/react";
import Login from "@/app/(auth)/login/page";

describe("Login Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render login page", async () => {
        const {container} =  render(<Login />);
        expect(container).toBeDefined();
        await expect(container).toMatchFileSnapshot('./__snapshots__/loginPageTest.html');
    })
});