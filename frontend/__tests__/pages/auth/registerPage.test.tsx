import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import Register from "@/app/(auth)/register/page";

describe("Register Page", () => {

    it("should render register page", () => {
        render(<Register/>);
        expect(screen.getByText('Register')).toBeDefined();
    })
});