'use client'

import React, {useState} from 'react';
import styles from './stylesAddUser.module.css';
import Input from '@/components/ui/input/input';
import Button from '@/components/ui/buttons/button/Button';

interface AddUserProps {
    onSubmit: (userData: { firstName: string; lastName: string; email: string }) => void;
}

const AddUser: React.FC<AddUserProps> = ({onSubmit}) => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');

    const handleClick = () => {
        onSubmit({firstName, lastName, email});
    };

    return (
        <div className={styles.form}>
            <p className={styles.title}>Prénom</p>
            <Input type='text' placeholder='Prénom...' value={firstName}
                   onChange={(e) => setFirstName(e.target.value)}/>
            <p className={styles.title}>Nom</p>
            <Input type='text' placeholder='Nom...' value={lastName} onChange={(e) => setLastName(e.target.value)}/>
            <p className={styles.title}>Email</p>
            <Input type='email' placeholder='Email...' value={email} onChange={(e) => setEmail(e.target.value)}/>
            <Button title='Ajouter' action={handleClick} type='primary' size='full'/>
        </div>
    );
};

export default AddUser;
