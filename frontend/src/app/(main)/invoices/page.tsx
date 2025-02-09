'use client';
import React, {useEffect, useState} from 'react';
import DataTable, {TableColumn} from 'react-data-table-component';
import styles from '@/components/ui/tableau/styles.module.css';
import Button from '@/components/ui/buttons/button/Button';
import IconButton from "@/components/ui/buttons/icon-button/IconButton";
import Link from "next/link";
import {sampleInvoices} from "@/lib/api/temporaryData/invoices/invoices";
import {Invoice} from "@/lib/types/invoices/invoices";
import {getInvoiceStatusBadge} from "@/lib/constants/invoices";




const InvoiceTable: React.FC = () => {
    const [isMounted, setIsMounted] = useState(false);
    const [filterText, setFilterText] = useState('');

    useEffect(() => {
        setIsMounted(true);
    }, []);


    const columns: TableColumn<Invoice>[] = [
        {
            name: 'Id',
            selector: (row) => row.id,
            sortable: true,
        },
        {
            name: 'Client',
            selector: (row) => row.billingInfo.firstName + ' ' + row.billingInfo.lastName,
            sortable: true,
        },
        {
            name: 'Prix',
            selector: (row) => `${row.totalAmount.value} ${row.totalAmount.currency}`,
            sortable: true,
        },
        {
            name: 'Status',
            cell: (row) => getInvoiceStatusBadge(row.status),
            sortable: true,
        },
        {
            name: 'Actions',
            cell: (row) => (
                <div className={styles.actionIcons}>
                    <Link href={`/invoices/${row.id}`}>
                        <IconButton icon={'Eye'} onClick={() => console.log('')}/>
                    </Link>
                </div>
            ),
            ignoreRowClick: true,
        },
    ];

    const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
        const searchText = event.target.value;
        setFilterText(searchText);
    };

    const filteredInvoices = sampleInvoices.filter((invoice: Invoice) =>
        invoice.billingInfo.firstName.toLowerCase().includes(filterText.toLowerCase()) ||
        invoice.status.toLowerCase().includes(filterText.toLowerCase())
    );

    if (!isMounted) {
        return null;
    }

    return (
        <div className={styles.ctn}>
            <div className={styles.ctn1}>
                <input
                    type="text"
                    placeholder="Rechercher..."
                    value={filterText}
                    onChange={handleSearch}
                    className={styles.searchInput}
                />
                <Button icon={'Filter'} title='Filtrer' action={() => {
                }}/>
            </div>

            <DataTable
                className={styles.table}
                columns={columns}
                data={filteredInvoices}
                pagination
                highlightOnHover
                striped
                customStyles={{
                    headRow: {
                        style: {
                            backgroundColor: '#4A6741',
                            color: 'white',
                            borderRadius: '5px 5px 0 0',
                        },
                    },
                    pagination: {
                        style: {
                            backgroundColor: '#4A6741',
                            color: 'white',
                        },
                        pageButtonsStyle: {
                            "&:disabled": {
                                color: 'white',
                                fill: 'white',
                            },
                        },
                    },
                    rows: {
                        style: {
                            color: 'black',
                        },
                        highlightOnHoverStyle: {
                            backgroundColor: 'none',
                            color: 'black',
                        },
                        stripedStyle: {
                            backgroundColor: '#F5F1E8',
                        },
                    },
                    table: {
                        style: {
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                            borderRadius: '8px',
                            border: '1px solid #ddd',
                        },
                    },
                }}
            />
        </div>
    );
};

export default InvoiceTable;
