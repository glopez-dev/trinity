import {afterEach, describe, expect, it} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import AddUserPage from "@/app/(main)/addUser/page";

describe("Dashboard Page", () => {
    afterEach(() => {
        cleanup();
    });

    it("should render dashboard page", () => {
        render(<AddUserPage />);
        expect(screen.getByText('Ajouter un utilisateur')).toBeDefined();
    })
});