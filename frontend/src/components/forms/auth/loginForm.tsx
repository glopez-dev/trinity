import styles from '@/styles/auth/login.module.css';
import Input from "@/components/ui/input/input";
import Button from "@/components/ui/buttons/button/Button";
import React, {useState} from "react";
import {type Login, loginSchema} from "@/lib/schemas/userSchema";
import {InputValueTypes} from "@/components/ui/input/types";

export default function LoginForm() {
    const [user, setUser] = useState<Login>({
        email: '',
        password: ''
    })

    const handleInputChange = (name: string, value: InputValueTypes) => {
        setUser({
            ...user,
            [name]: value
        });
    };


    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            loginSchema.parse(user);
        } catch (e) {
            console.log(e);
        }
        console.log('submit');
    }


    return (
        <form onSubmit={handleSubmit} className={styles.loginCard}>
            <div className={styles.loginCardHeader}>
                <h1>Connexion</h1>
                <p>
                    Pilotez votre activité avec précision et prenez les meilleures décisions.
                </p>
            </div>
            <Input
                name={'email'}
                type={'email'}
                placeholder={'Email'}
                value={user.email}
                onChange={(value) => handleInputChange('email', value)}
                schema={loginSchema.shape.email}
                required={true}
            />
            <Input
                name={'password'}
                type={'password'}
                placeholder={'Mot de passe'}
                value={user.password}
                onChange={(value) => handleInputChange('password', value)}
                schema={loginSchema.shape.password}
                required={true}
            />
            <Button title={'Connexion'} type={'submit'} size={'full'}/>
        </form>
    );
}