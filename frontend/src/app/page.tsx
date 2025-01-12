'use client'
import Input from "@/components/ui/input/input";


export default function Home() {
    return (
        <div>
            <h1>Home</h1>
            <Input
                label={"Email"}
                type={"email"}
                placeholder={"Saisir..."}
                regex={/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/}
                required={true}
            />
        </div>
    );
}
