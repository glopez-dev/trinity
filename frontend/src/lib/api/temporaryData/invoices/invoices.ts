import {Invoice} from "@/lib/types/invoices/invoices";

const sampleInvoices: Invoice[] = [
    {
        id: "INV2-QXWN-W3VH-Q8H7-XH8J",
        status: "PAID",
        totalAmount: {
            value: 250.00,
            currency: "USD"
        },
        merchantInfo: {
            email: "merchant@example.com",
            firstName: "John",
            lastName: "Doe"
        },
        billingInfo: {
            email: "customer@example.com",
            firstName: "Jane",
            lastName: "Doe"
        },
        items: [
            {
                name: "Item 1",
                quantity: 2,
                unitPrice: 55.00
            },
            {
                name: "Item 2",
                quantity: 1,
                unitPrice: 150.00
            }
        ]
    },
    {
        id: "INV2-LPQM-N5RT-K9F2-YU3V",
        status: "PENDING",
        totalAmount: {
            value: 375.50,
            currency: "EUR"
        },
        merchantInfo: {
            email: "store@example.com",
            firstName: "Marie",
            lastName: "Martin"
        },
        billingInfo: {
            email: "client@example.com",
            firstName: "Pierre",
            lastName: "Dubois"
        },
        items: [
            {
                name: "Product A",
                quantity: 3,
                unitPrice: 75.50
            },
            {
                name: "Product B",
                quantity: 2,
                unitPrice: 74.50
            }
        ]
    },
    {
        id: "INV2-HTKS-P7VN-M4B8-ZW9C",
        status: "PAID",
        totalAmount: {
            value: 890.00,
            currency: "USD"
        },
        merchantInfo: {
            email: "shop@example.com",
            firstName: "David",
            lastName: "Smith"
        },
        billingInfo: {
            email: "buyer@example.com",
            firstName: "Sarah",
            lastName: "Johnson"
        },
        items: [
            {
                name: "Premium Service",
                quantity: 1,
                unitPrice: 890.00
            }
        ]
    },
    {
        id: "INV2-JRWX-T9BN-Q2H5-UV6M",
        status: "CANCELLED",
        totalAmount: {
            value: 150.75,
            currency: "EUR"
        },
        merchantInfo: {
            email: "vendor@example.com",
            firstName: "Sophie",
            lastName: "Bernard"
        },
        billingInfo: {
            email: "customer2@example.com",
            firstName: "Lucas",
            lastName: "Petit"
        },
        items: [
            {
                name: "Item X",
                quantity: 3,
                unitPrice: 35.25
            },
            {
                name: "Item Y",
                quantity: 1,
                unitPrice: 45.00
            }
        ]
    },
    {
        id: "INV2-MKNP-R4FS-L7D1-XC8B",
        status: "PAID",
        totalAmount: {
            value: 1250.00,
            currency: "USD"
        },
        merchantInfo: {
            email: "business@example.com",
            firstName: "Michael",
            lastName: "Brown"
        },
        billingInfo: {
            email: "corp@example.com",
            firstName: "Emily",
            lastName: "Wilson"
        },
        items: [
            {
                name: "Consulting Service",
                quantity: 5,
                unitPrice: 250.00
            }
        ]
    },
    {
        id: "INV2-BVCT-K8HM-W3N9-YP4Q",
        status: "PENDING",
        totalAmount: {
            value: 445.90,
            currency: "EUR"
        },
        merchantInfo: {
            email: "seller@example.com",
            firstName: "Thomas",
            lastName: "Garcia"
        },
        billingInfo: {
            email: "buyer2@example.com",
            firstName: "Emma",
            lastName: "Martinez"
        },
        items: [
            {
                name: "Product X1",
                quantity: 2,
                unitPrice: 120.95
            },
            {
                name: "Product X2",
                quantity: 1,
                unitPrice: 204.00
            }
        ]
    },
    {
        id: "INV2-QSXW-P9VN-M2B7-ZK4H",
        status: "PAID",
        totalAmount: {
            value: 780.25,
            currency: "USD"
        },
        merchantInfo: {
            email: "company@example.com",
            firstName: "William",
            lastName: "Taylor"
        },
        billingInfo: {
            email: "client2@example.com",
            firstName: "Oliver",
            lastName: "Anderson"
        },
        items: [
            {
                name: "Service Package",
                quantity: 1,
                unitPrice: 550.25
            },
            {
                name: "Add-on Service",
                quantity: 2,
                unitPrice: 115.00
            }
        ]
    },
    {
        id: "INV2-LNBV-T6CM-K1F8-XH3W",
        status: "PAID",
        totalAmount: {
            value: 325.50,
            currency: "EUR"
        },
        merchantInfo: {
            email: "store2@example.com",
            firstName: "Julie",
            lastName: "Robert"
        },
        billingInfo: {
            email: "customer3@example.com",
            firstName: "Antoine",
            lastName: "Leroy"
        },
        items: [
            {
                name: "Item Alpha",
                quantity: 5,
                unitPrice: 65.10
            }
        ]
    },
    {
        id: "INV2-HKJM-R7WN-P3B9-YT5V",
        status: "PENDING",
        totalAmount: {
            value: 567.80,
            currency: "USD"
        },
        merchantInfo: {
            email: "merchant2@example.com",
            firstName: "James",
            lastName: "Wilson"
        },
        billingInfo: {
            email: "buyer3@example.com",
            firstName: "Isabella",
            lastName: "Moore"
        },
        items: [
            {
                name: "Product Z1",
                quantity: 2,
                unitPrice: 156.90
            },
            {
                name: "Product Z2",
                quantity: 3,
                unitPrice: 84.70
            }
        ]
    },
    {
        id: "INV2-PFQS-N4BK-L8H2-WM7C",
        status: "CANCELLED",
        totalAmount: {
            value: 920.00,
            currency: "EUR"
        },
        merchantInfo: {
            email: "business2@example.com",
            firstName: "Paul",
            lastName: "Durand"
        },
        billingInfo: {
            email: "corp2@example.com",
            firstName: "Claire",
            lastName: "Martin"
        },
        items: [
            {
                name: "Premium Package",
                quantity: 1,
                unitPrice: 750.00
            },
            {
                name: "Support Service",
                quantity: 2,
                unitPrice: 85.00
            }
        ]
    }
];

export { sampleInvoices };