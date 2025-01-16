'use client';
import React from 'react';
import styles  from './styles.module.css';
import Form from '@/components/forms/users/formUser';


const AddUserPage = () => {
    const handleAddUser = (userData: { firstName: string; lastName: string; email: string }) => {
        console.log('User Data:', userData);
    };

    return (
        <div>
            <h1 className={styles.title}>Ajouter un utilisateur</h1>
            <Form onSubmit={handleAddUser} />
        </div>
    );
};

export default AddUserPage;
