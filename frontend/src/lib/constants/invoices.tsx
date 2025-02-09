import Badge from "@/components/ui/badge/Badge";
import React from "react";

export const PAID = 'PAID';
export const PENDING = 'PENDING';
export const CANCELLED = 'CANCELLED';

export const InvoiceStatus = {
    PAID : 'Paid',
    PENDING: 'Pending',
    CANCELLED: 'Cancelled',
}


export const getInvoiceStatusBadge = (status: string) => {
    switch (status) {
        case PAID:
            return <Badge text={InvoiceStatus.PAID} type="success"/>;
        case PENDING:
            return <Badge text={InvoiceStatus.PENDING} type="info"/>;
        case CANCELLED:
            return <Badge text={InvoiceStatus.CANCELLED} type="error"/>;
        default:
            return <Badge text="Unknown" type="warning"/>;
    }
};