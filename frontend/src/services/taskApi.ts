import axios from "axios";
import type { TaskRequest, TaskResponse } from "../types/index"

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1",  // Fallback for no env
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: false,
});

export const addTask = async (taskRequest: TaskRequest): Promise<string> => {
    const { data } = await api.post<string>("/task", taskRequest);
    return data;
}

export const fetchRecentTasks = async (): Promise<TaskResponse[]> => {
    const { data } = await api.get<TaskResponse[]>("/task");
    return data;
}

export const updateTaskStatus = async (id: number, completed: boolean): Promise<string> => {
    const { data } = await api.put<string>(`/task/${id}`, null, { params: { completed }, });
    return data;
}
