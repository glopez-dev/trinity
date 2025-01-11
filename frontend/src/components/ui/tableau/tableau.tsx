"use client";
import React, { useState } from 'react';
import DataTable, { TableColumn } from 'react-data-table-component';
import {icons} from 'lucide-react';
import styles from './styles.module.css';
import Button from '@/components/ui/buttons/button/Button';


interface User {
  firstName: string;
  lastName: string;
  email: string;
  id: number;
}



const usersData: User[] = [
  { id: 1,firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com' },
  { id: 2,firstName: 'Jane', lastName: 'Smith', email: 'jane.smith@example.com' },
  { id: 3,firstName: 'Alice', lastName: 'Johnson', email: 'alice.johnson@example.com' },
  { id: 3,firstName: 'Alice', lastName: 'Johnson', email: 'alice.johnson@example.com' },
  
];


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
      <div className="actionIcons">
        <icons.Pencil className={styles.editIcon} onClick={() => handleEdit(row)} aria-label="Edit user" />
        <icons.Trash className={styles.deleteIcon} onClick={() => handleDelete(row)} aria-label="Delete user" />
        
      </div>
    ),
    ignoreRowClick: true,
    allowOverflow: true,
    button: true,
  },
];

const handleDelete = (user: User) => {
  console.log('Suppression de l’utilisateur:', user);
  
};


const handleEdit = (user: User) => {
  console.log('Édition de l’utilisateur:', user);
 
};

const UserTable: React.FC = () => {
  const [filterText, setFilterText] = useState('');
  const [filteredUsers, setFilteredUsers] = useState(usersData);


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

        <Button icon={'Filter'} title='Filtrer'  action={() => {}} />

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
                
                color : "white", 
                fill : "white" 
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
    </div>


   
  );
};

export default UserTable;
