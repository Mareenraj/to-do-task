import React from 'react';
import type {TaskResponse} from '../types';
import "../styles/TaskList.css";

interface TaskListProps {
    tasks: TaskResponse[];
    onToggleComplete: (id: number) => void;
}

const TaskList: React.FC<TaskListProps> = ({tasks, onToggleComplete}) => {
    const uncompletedTasks = tasks.filter(task => !task.completed);

    return (
        <div className="task-list">
            {uncompletedTasks.map((task) => (  // Map only uncompleted
                <div key={task.id} className={`task-card ${task.completed ? 'completed' : ''}`}>
                    <div className="task-header">
                        <div>
                            <h3 className={`task-title ${task.completed ? 'completed' : ''}`}>
                                {task.title}
                            </h3>
                            {task.description && (
                                <p className={`task-description ${task.completed ? 'completed' : ''}`}>
                                    {task.description}
                                </p>
                            )}
                        </div>
                        <button
                            onClick={() => onToggleComplete(task.id)}
                            className={`done-button ${task.completed ? 'completed' : ''}`}
                        >
                            {task.completed ? 'Undo' : 'Done'}
                        </button>
                    </div>
                </div>
            ))}
            {uncompletedTasks.length === 0 && (
                <p>No active tasks found. Add a task to begin.</p>
            )}
        </div>
    );
};

export default TaskList;