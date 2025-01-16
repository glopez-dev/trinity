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
            <Input label={'Prénom'} type='text' placeholder='Prénom...' value={firstName} name={'firstName'}
                   onChange={(e) => setFirstName(e.target.value)}/>
            <Input label={'Nom'} type='text' placeholder='Nom...' value={lastName} name={'lastName'}
                   onChange={(e) => setLastName(e.target.value)}/>
            <Input label={'Email'} type='email' placeholder='Email...' value={email} name={'email'}
                   onChange={(e) => setEmail(e.target.value)}/>
            <Button title='Ajouter' action={handleClick} type='primary' size='full'/>
        </div>
    );
};

export default AddUser;
