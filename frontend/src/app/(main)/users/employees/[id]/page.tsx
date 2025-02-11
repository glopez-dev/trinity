'use client';

import {useEffect, useState} from 'react';

import {Employee} from "@/lib/types/user/employees";
import {employees} from "@/lib/api/temporaryData/users/employees";
import styles from '@/styles/employees/employees.module.css';

export default function EmployeeDetailPage({params}: Readonly<Readonly<{
    params: Promise<{ id: string }>
}>>) {
    const [employee, setEmployee] = useState<Employee | null>(null);
    const [id, setId] = useState<string | null>(null);

    useEffect(() => {
        params.then(result => {
            setId(result.id);
        });
    }, [params]);

    useEffect(() => {
        const foundEmployee = employees.find((emp) => emp.id === id);
        if (foundEmployee) {
            const employeeData: Employee = {
                ...foundEmployee,
                firstName: foundEmployee.personalInfo.firstName,
                lastName: foundEmployee.personalInfo.lastName,
            };
            setEmployee(employeeData);
        } else {
            setEmployee(null);
        }
    }, [id]);

    return (


        <div>
            <div className={styles.titre}>
                <h1>Fiche Employé</h1>
            </div>
            {employee ? (
                <div className={styles.details}>
                    <h2>{employee.firstName} {employee.lastName}</h2>
                    <p><strong>Email:</strong> {employee.email}</p>
                    <p><strong>Téléphone:</strong> {employee.personalInfo.phoneNumber}</p>
                    <p><strong>Rôle:</strong> {employee.role}</p>
                    <p><strong>{'Date d\'embauche:'}</strong> {employee.hireDate}</p>
                </div>
            ) : (
                <p>Employé non trouvé</p>
            )}
        </div>

    );
}
