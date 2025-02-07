import styles from '@/styles/auth/login.module.css';
import Input from "@/components/ui/input/input";
import Button from "@/components/ui/buttons/button/Button";
import React, {useState} from "react";
import {type Login, loginSchema} from "@/lib/schemas/userSchema";
import {InputValueTypes} from "@/components/ui/input/types";
import axios from "axios";
import {useFlash} from "@/lib/contexts/FlashMessagesContext";
import {login} from "@/lib/actions/auth";
import {useAuth} from "@/lib/contexts/AuthContext";
import {useRouter} from "next/navigation";

export default function LoginForm() {
    const {showMessage} = useFlash();
    const {setToken} = useAuth();
    const router = useRouter();

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

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            loginSchema.parse(user);
            const response = await login(user);

            if (response instanceof Error) {
                showMessage('error', response.message);
                return;
            }
            setToken(response.jwt);

            router.replace('/dashboard');
            router.refresh();

        } catch (error) {
            if (axios.isAxiosError(error)) {
                showMessage('error', error?.message);
            } else {
                showMessage('error', 'Une erreur est survenue !');
            }
        }
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