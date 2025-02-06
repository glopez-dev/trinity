import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import AddUserPage from "@/app/(main)/addUser/page";

describe("Dashboard Page", () => {

    it("should render dashboard page", () => {
        render(<AddUserPage/>);
        expect(screen.getByText('Ajouter un utilisateur')).toBeDefined();
    })
});