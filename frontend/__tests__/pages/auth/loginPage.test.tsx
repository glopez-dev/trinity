import {describe, expect, it} from "vitest";
import Login from "@/app/(auth)/login/page";
import {renderWithProviders} from "@test/test-utils";

describe("Login Page", () => {

    it("should render login page", async () => {
        const {container} = renderWithProviders(<Login/>);
        expect(container).toBeDefined();
        await expect(container).toMatchFileSnapshot('./__snapshots__/loginPageTest.html');
    })
});