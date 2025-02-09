interface Amount {
    value: number;
    currency: string;
}

interface PersonInfo {
    email: string;
    firstName: string;
    lastName: string;
}

interface InvoiceItem {
    name: string;
    quantity: number;
    unitPrice: number;
}

interface Invoice {
    id: string;
    status: 'PAID' | 'PENDING' | 'CANCELLED';
    totalAmount: Amount;
    merchantInfo: PersonInfo;
    billingInfo: PersonInfo;
    items: InvoiceItem[];
}

export type {Invoice};