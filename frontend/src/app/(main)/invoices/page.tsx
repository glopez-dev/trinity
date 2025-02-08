'use client';
import React, { useState} from 'react';
import DataTable, { TableColumn } from 'react-data-table-component';
import styles from '@/components/ui/tableau/styles.module.css';
import Button from '@/components/ui/buttons/button/Button';
import IconButton from "@/components/ui/buttons/icon-button/IconButton";
import Badge from "@/components/ui/badge/Badge";
import invoicesData from '@/lib/api/temporaryData/invoices/invoice.json'; 

interface Invoice {
    id: number;
    firstName: string;
    prix: number;
    status: string; 
}

const getStatusBadge = (status: string) => {
    switch (status) {
        case 'Active':
            return <Badge text="Active" type="success" />;
        case 'Pending':
            return <Badge text="Pending" type="warning" />;
        case 'Cancelled':
            return <Badge text="Cancelled" type="error" />;
        default:
            return <Badge text="Unknown" type="info" />;
    }
};

const columns: TableColumn<Invoice>[] = [
    {
        name: 'Id',
        selector: (row) => row.id,
        sortable: true,
    },
    {
        name: 'Prénom',
        selector: (row) => row.firstName,
        sortable: true,
    },
    {
        name: 'Prix',
        selector: (row) => `${row.prix}$`,
        sortable: true,
    },
    {
        name: 'Status',
        cell: (row) => getStatusBadge(row.status), // Corrected to use getStatusBadge function
        sortable: true,
    },
    {
        name: 'Actions',
        cell: (row) => (
            <div className={styles.actionIcons}>
                <IconButton icon={'Pencil'} onClick={() => handleEdit(row)} />
                <IconButton icon={'Trash'} onClick={() => handleDelete(row)} />
            </div>
        ),
        ignoreRowClick: true,
    },
];

const handleDelete = (invoice: Invoice) => {
    console.log('Suppression de la facture:', invoice);
};

const handleEdit = (invoice: Invoice) => {
    console.log('Édition de la facture:', invoice);
};

const InvoiceTable: React.FC = () => {
    const [filterText, setFilterText] = useState('');

    const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
        const searchText = event.target.value;
        setFilterText(searchText);
    };

    const filteredInvoices = invoicesData.filter((invoice: Invoice) =>
        invoice.firstName.toLowerCase().includes(filterText.toLowerCase()) ||
        invoice.status.toLowerCase().includes(filterText.toLowerCase())
    );

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
                <Button icon={'Filter'} title='Filtrer' action={() => {}} />
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
