import React, {useEffect, useState} from 'react';
import {Sidebar} from './components/Sidebar';
import {Topbar} from './components/Topbar';

export default function Navigation() {
    const [isOpen, setIsOpen] = useState(true);
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const handleResize = () => {
            const isMobileView = window.innerWidth < 768;
            setIsMobile(isMobileView);
            if (isMobileView) {
                setIsOpen(false);
            }
        };

        handleResize();
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    const toggleMenu = () => setIsOpen(!isOpen);

    if (isMobile) {
        return <Topbar isOpen={isOpen} onToggle={toggleMenu}/>
    } else {
        return <Sidebar isOpen={isOpen} onToggle={toggleMenu}/>
    }
};