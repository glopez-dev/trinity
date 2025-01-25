import React from 'react';
import styles from './badge.module.css';

type BadgeType = 'success' | 'warning' | 'error' | 'info';

interface BadgeProps {
    text: string;
    type: BadgeType;
}

const Badge = ({text, type}: BadgeProps) => {
    return (
        <div role={'badge'} className={`${styles[type]} ${styles.badge}`}>
            <span>{text}</span>
        </div>
    );
};

export default Badge;