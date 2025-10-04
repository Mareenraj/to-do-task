export interface TaskResponse {
    id: number;
    title: string;
    description?: string;
    completed: boolean;
}

export interface TaskRequest {
    title: string;
    description?: string;
}