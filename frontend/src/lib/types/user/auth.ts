export interface LoginResponse {
    jwt: string;
}

export type AuthContextType = {
    setToken: (jwt: string) => void;
    logout: () => void;
    checkAuth: () => boolean;
};
