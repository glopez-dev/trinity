import React from 'react';
import styles from './stylesAddUser.module.css';

const addUser = () => {
    return (
        <div>
            <h1 className={styles.title}>Add User</h1>

            <div className={styles.inputContainer}>

                <p className={styles.name}>Prénom</p>
            <input
          type="text"
          placeholder="Prénom..."
          className={styles.input}
        />
        <p className={styles.name}>Nom</p>
           <input
          type="text"
          placeholder="Nom..."
          className={styles.input}
        />
        <p className={styles.name}>Email</p>
           <input
          type="email"
          placeholder="Email..."
          className={styles.input}
        />

<button className={styles.button}>Ajouter</button>
        </div>

           

            
        </div>
    );
};

export default addUser;