'use client';
import React, {useEffect, useState} from 'react';
import DataTable, {TableColumn} from 'react-data-table-component';
import styles from './styles.module.css';
import Button from '@/components/ui/buttons/button/Button';
import IconButton from "@/components/ui/buttons/icon-button/IconButton";
import Modal from '@/components/ui/modal/Modal';
import Link from 'next/link';
import {usePathname, useRouter} from "next/navigation";

interface User {
    firstName: string;
    lastName: string;
    email: string;
    id: number;
}

const usersData: User[] = [
    {id: 1, firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com'},
    {id: 2, firstName: 'Jane', lastName: 'Smith', email: 'jane.smith@example.com'},
    {id: 3, firstName: 'Alice', lastName: 'Johnson', email: 'alice.johnson@example.com'},
];

const UserTable: React.FC = () => {
    const [filterText, setFilterText] = useState('');
    const [filteredUsers, setFilteredUsers] = useState<User[]>([]);
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const router = useRouter();
    const pathname = usePathname();

    useEffect(() => {
        setFilteredUsers(usersData);
    }, []);

    const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
        const searchText = event.target.value;
        setFilterText(searchText);

        const filteredData = usersData.filter(
            (user) =>
                user.firstName.toLowerCase().includes(searchText.toLowerCase()) ||
                user.lastName.toLowerCase().includes(searchText.toLowerCase()) ||
                user.email.toLowerCase().includes(searchText.toLowerCase())
        );

        setFilteredUsers(filteredData);
    };

    const handleDeleteClick = (user: User) => {
        setSelectedUser(user);
        setModalVisible(true);
    };

    const confirmDelete = () => {
        if (selectedUser) {
            console.log(`Utilisateur supprimé:`, selectedUser);
            setFilteredUsers(filteredUsers.filter((user) => user.id !== selectedUser.id));
        }
        setModalVisible(false);
    };

    const columns: TableColumn<User>[] = [
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
            name: 'Nom',
            selector: (row) => row.lastName,
            sortable: true,
        },
        {
            name: 'Email',
            selector: (row) => row.email,
            sortable: true,
        },
        {
            name: 'Actions',
            cell: (row) => (
                <div className={styles.actionIcons}>
                    <IconButton icon={'Eye'} onClick={() => {
                        if (pathname === '/users/employees') {
                            router.push(`/users/employees/${row.id}`);
                        } else {
                            router.push(`/users/customers/${row.id}`);
                        }
                    }}/>
                    <IconButton icon={'Trash'} onClick={() => handleDeleteClick(row)}/>
                </div>
            ),
            ignoreRowClick: true,
        },
    ];

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
                <IconButton icon={'Filter'} onClick={() => console.log('filter')}/>
                <Link href="/users/employees/create">
                    <IconButton icon={'Plus'} onClick={() => console.log('Plus')}/>
                </Link>
            </div>

            <DataTable
                className={styles.table}
                columns={columns}
                data={filteredUsers}
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
                                color: "white",
                                fill: "white"
                            },
                        }
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
                        }
                    },
                    table: {
                        style: {
                            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
                            borderRadius: "8px",
                            border: "1px solid #ddd",
                        }
                    }
                }}
            />


            <Modal
                title="Confirmer la suppression"
                body={<p>Êtes-vous sûr de vouloir supprimer {selectedUser?.firstName} {selectedUser?.lastName} ?</p>}
                footer={
                    <div className={styles.buttons}>
                        <Button
                            title={'Supprimer'}
                            action={() => {
                                confirmDelete();
                            }}
                            color={'accent'}

                        />
                        <Button
                            title={'Annuler'}
                            color={'secondary'}
                            action={() => setModalVisible(false)}
                        />
                    </div>
                }
                modalVisible={modalVisible}
                setModalVisible={setModalVisible}
            />
        </div>
    );
};

export default UserTable;
