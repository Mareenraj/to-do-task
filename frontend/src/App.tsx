import { useState, useEffect } from 'react'
import TaskForm from "./components/TaskForm";
import TaskList from "./components/TaskList";
import { addTask, fetchAllTasks, updateTaskStatus } from "./services/taskApi";
import type { TaskRequest, TaskResponse } from "./types";

const App: React.FC = () => {

  const [tasks, setTasks] = useState<TaskResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadTasks = async () => {
      try {
        setLoading(true);
        setError(null);
        const fetchedTasks = await fetchAllTasks();
        setTasks(fetchedTasks);
      } catch (err) {
        setError('Failed to load tasks. Please try again.');
        console.error('Error fetching tasks:', err);
      } finally {
        setLoading(false);
      }
    };

    loadTasks();
  }, []); 

  const handleAddTask = async (title: string, description: string) => {
    try {
      setError(null);
      const taskData: TaskRequest = { title, description };
      await addTask(taskData);

      const updatedTasks = await fetchAllTasks();
      setTasks(updatedTasks);
    } catch (err) {
      setError('Failed to add task. Please try again.');
      console.error('Error adding task:', err);
    }
  };

  const handleToggleComplete = async (id: number) => {
    try {
      setError(null);
      const taskToUpdate = tasks.find((t) => t.id === id);
      if (!taskToUpdate) return;

      await updateTaskStatus(id, !taskToUpdate.completed);

      const updatedTasks = await fetchAllTasks();
      setTasks(updatedTasks);
    } catch (err) {
      setError('Failed to update task. Please try again.');
      console.error('Error updating task:', err);
    }
  };

  if (loading) {
    return (
      <div>
        <h1>To Do App</h1>
        <p>Loading tasks...</p>
      </div>
    );
  }

  return (
    <div>
      <h1>To Do App</h1>
      {error && <p>{error}</p>}

      <TaskForm onAdd={handleAddTask} />
      <TaskList tasks={tasks} onToggleComplete={handleToggleComplete} />
    </div>
  );
};

export default App;