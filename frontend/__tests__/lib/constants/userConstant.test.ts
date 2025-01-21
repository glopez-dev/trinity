import {describe, expect, it} from "vitest";
import {USER_ROLE, USER_STATUS} from "@/lib/constants/users";

describe("User Constants", () => {
    it('should user status have good values', () => {
        expect(USER_STATUS.ACTIVE).eq(1);
        expect(USER_STATUS.INACTIVE).eq(2);
        expect(USER_STATUS.DELETED).eq(3);
    });

    it('should role have good values', () => {
        expect(USER_ROLE.ADMIN).eq('ADMIN');
        expect(USER_ROLE.CUSTOMER).eq('CUSTOMER');
        expect(USER_ROLE.EMPLOYEE).eq('EMPLOYEE');
    });
});
