'use client';
import React from 'react';
import styles  from './styles.module.css';
import AddUser from "@/components/forms/users/formUser";
import {InputValueTypes} from "@/components/ui/input/types";


const AddUserPage = () => {
    const handleAddUser = (userData: { firstName: InputValueTypes; lastName: InputValueTypes; email: InputValueTypes }) => {
        console.log('User Data:', userData);
    };

    return (
        <div>
            <h1 className={styles.title}>Ajouter un utilisateur</h1>
            <AddUser onSubmit={handleAddUser} />
        </div>
    );
};

export default AddUserPage;
