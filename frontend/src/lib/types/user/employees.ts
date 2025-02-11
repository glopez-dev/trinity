import { ReactNode } from "react";

export interface Employee {
    lastName: ReactNode;
    firstName: ReactNode;
    id: string;
    email: string;
    personalInfo: {
        firstName: string;
        lastName: string;
        phoneNumber: string;
    };
    role: string;
    status: number;
    hireDate: string;
    createdAt: string;
    updatedAt: string;
    lastLoginAt: string;
}
