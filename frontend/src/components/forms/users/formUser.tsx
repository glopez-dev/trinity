'use client'

import React, {useState} from 'react';
import styles from './stylesAddUser.module.css';
import Input from '@/components/ui/input/input';
import Button from '@/components/ui/buttons/button/Button';
import {InputValueTypes} from "@/components/ui/input/types";

interface AddUserProps {
    onSubmit: (userData: { firstName: InputValueTypes; lastName: InputValueTypes; email: InputValueTypes }) => void;
}

const AddUser: React.FC<AddUserProps> = ({onSubmit}) => {
    const [firstName, setFirstName] = useState<InputValueTypes>('');
    const [lastName, setLastName] = useState<InputValueTypes>('');
    const [email, setEmail] = useState<InputValueTypes>('');

    const handleClick = () => {
        onSubmit({firstName, lastName, email});
    };

    return (
        <div className={styles.form}>
            <Input
                label={'Prénom'}
                type='text'
                placeholder='Prénom...'
                value={firstName}
                name={'firstName'}
                onChange={(value) => setFirstName(value)}
            />
            <Input
                label={'Nom'}
                type='text'
                placeholder='Nom...'
                value={lastName}
                name={'lastName'}
                onChange={(value) => setLastName(value)}
            />
            <Input
                label={'Email'}
                type='email'
                placeholder='Email...'
                value={email}
                name={'email'}
                onChange={(value) => setEmail(value)}
            />
            <Button title='Ajouter' action={handleClick} color='primary' type={'submit'} size='full'/>
        </div>
    );
};

export default AddUser;
