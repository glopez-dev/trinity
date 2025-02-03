import {afterEach, describe, expect, it} from "vitest";
import {cleanup} from "@testing-library/react";
import Login from "@/app/(auth)/login/page";
import {renderWithProviders} from "@test/test-utils";

describe("Login Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render login page", async () => {
        const {container} =  renderWithProviders(<Login />);
        expect(container).toBeDefined();
        await expect(container).toMatchFileSnapshot('./__snapshots__/loginPageTest.html');
    })
});