"use client";

import styles from '@/styles/auth/login.module.css';
import LoginForm from "@/components/forms/auth/loginForm";
import Image from "next/image";
import LoginGreenCard from "@/components/features/auth/loginGreenCard";

export default function Login() {
    return (
        <div className={styles.loginContainer}>
            <div className={styles.loginFormContainer}>
                <Image
                    src={'images/logo-svg-trinity-green.svg'}
                    alt={'trinity logo'}
                    width={180}
                    height={60}
                />
                <LoginForm/>
            </div>
            <div className={styles.loginRightContainer}>
                <LoginGreenCard />
            </div>
        </div>
    );
}