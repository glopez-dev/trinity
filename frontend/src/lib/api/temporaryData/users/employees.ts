import {Employee} from "@/lib/types/user/employees";

export const employees = [
    {
      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "email": "sophie.martin@store.com",
      "personalInfo": {
        "firstName": "Sophie",
        "lastName": "Martin",
        "phoneNumber": "+33612345678"
      },
      "role": "STORE_MANAGER",
      "status": 1,
      "hireDate": "2022-03-15T09:00:00Z",
      "createdAt": "2022-03-14T14:30:00Z",
      "updatedAt": "2024-01-15T16:45:00Z",
      "lastLoginAt": "2024-01-20T08:15:00Z"
    },
    {
      "id": "e47ac10b-58cc-4372-a567-0e02b2c3d478",
      "email": "thomas.dubois@store.com",
      "personalInfo": {
        "firstName": "Thomas",
        "lastName": "Dubois",
        "phoneNumber": "+33623456789"
      },
      "role": "STORE_MANAGER",
      "status": 1,
      "hireDate": "2021-09-01T09:00:00Z",
      "createdAt": "2021-08-30T10:00:00Z",
      "updatedAt": "2024-01-18T11:30:00Z",
      "lastLoginAt": "2024-01-20T08:30:00Z"
    },
    {
      "id": "d47ac10b-58cc-4372-a567-0e02b2c3d477",
      "email": "julie.petit@store.com",
      "personalInfo": {
        "firstName": "Julie",
        "lastName": "Petit",
        "phoneNumber": "+33634567890"
      },
      "role": "CASHIER",
      "status": 1,
      "hireDate": "2023-01-10T09:00:00Z",
      "createdAt": "2023-01-09T15:00:00Z",
      "updatedAt": "2024-01-15T14:20:00Z",
      "lastLoginAt": "2024-01-20T13:45:00Z"
    },
    {
      "id": "c47ac10b-58cc-4372-a567-0e02b2c3d476",
      "email": "lucas.moreau@store.com",
      "personalInfo": {
        "firstName": "Lucas",
        "lastName": "Moreau",
        "phoneNumber": "+33645678901"
      },
      "role": "CASHIER",
      "status": 2,
      "hireDate": "2022-06-15T09:00:00Z",
      "terminationDate": "2024-01-15T18:00:00Z",
      "createdAt": "2022-06-14T11:00:00Z",
      "updatedAt": "2024-01-15T18:00:00Z",
      "lastLoginAt": "2024-01-15T17:30:00Z"
    },
    {
      "id": "b47ac10b-58cc-4372-a567-0e02b2c3d475",
      "email": "emma.bernard@store.com",
      "personalInfo": {
        "firstName": "Emma",
        "lastName": "Bernard",
        "phoneNumber": "+33656789012"
      },
      "role": "CASHIER",
      "status": 3,
      "hireDate": "2021-11-02T09:00:00Z",
      "terminationDate": "2023-12-31T18:00:00Z",
      "createdAt": "2021-11-01T14:00:00Z",
      "updatedAt": "2023-12-31T18:00:00Z",
      "lastLoginAt": "2023-12-31T17:45:00Z"
    },
    {
      "id": "a47ac10b-58cc-4372-a567-0e02b2c3d474",
      "email": "leo.rousseau@store.com",
      "personalInfo": {
        "firstName": "Léo",
        "lastName": "Rousseau",
        "phoneNumber": "+33667890123"
      },
      "role": "CASHIER",
      "status": 1,
      "hireDate": "2023-03-20T09:00:00Z",
      "createdAt": "2023-03-19T13:30:00Z",
      "updatedAt": "2024-01-17T09:15:00Z",
      "lastLoginAt": "2024-01-20T14:30:00Z"
    },
    {
      "id": "947ac10b-58cc-4372-a567-0e02b2c3d473",
      "email": "chloe.lambert@store.com",
      "personalInfo": {
        "firstName": "Chloé",
        "lastName": "Lambert",
        "phoneNumber": "+33678901234"
      },
      "role": "CASHIER",
      "status": 1,
      "hireDate": "2023-05-02T09:00:00Z",
      "createdAt": "2023-05-01T10:45:00Z",
      "updatedAt": "2024-01-16T16:20:00Z",
      "lastLoginAt": "2024-01-20T09:00:00Z"
    },
    {
      "id": "847ac10b-58cc-4372-a567-0e02b2c3d472",
      "email": "noah.dupont@store.com",
      "personalInfo": {
        "firstName": "Noah",
        "lastName": "Dupont",
        "phoneNumber": "+33689012345"
      },
      "role": "CASHIER",
      "status": 1,
      "hireDate": "2023-07-17T09:00:00Z",
      "createdAt": "2023-07-16T14:15:00Z",
      "updatedAt": "2024-01-19T11:45:00Z",
      "lastLoginAt": "2024-01-20T08:45:00Z"
    },
    {
      "id": "747ac10b-58cc-4372-a567-0e02b2c3d471",
      "email": "alice.girard@store.com",
      "personalInfo": {
        "firstName": "Alice",
        "lastName": "Girard",
        "phoneNumber": "+33690123456"
      },
      "role": "CASHIER",
      "status": 1,
      "hireDate": "2023-09-04T09:00:00Z",
      "createdAt": "2023-09-03T11:30:00Z",
      "updatedAt": "2024-01-18T15:10:00Z",
      "lastLoginAt": "2024-01-20T13:15:00Z"
    },
    {
      "id": "647ac10b-58cc-4372-a567-0e02b2c3d470",
      "email": "hugo.roux@store.com",
      "personalInfo": {
        "firstName": "Hugo",
        "lastName": "Roux",
        "phoneNumber": "+33691234567"
      },
      "role": "CASHIER",
      "status": 1,
      "hireDate": "2023-11-13T09:00:00Z",
      "createdAt": "2023-11-12T13:45:00Z",
      "updatedAt": "2024-01-19T14:25:00Z",
      "lastLoginAt": "2024-01-20T09:30:00Z"
    }
  ] as Employee[];