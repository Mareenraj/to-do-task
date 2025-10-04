import React from 'react';
import type { TaskResponse } from '../types';

interface TaskListProps {
    tasks: TaskResponse[];
    onToggleComplete: (id: number) => void;
}

const TaskList: React.FC<TaskListProps> = ({ tasks, onToggleComplete }) => {
    return (
        <div>  { }
            {tasks.map((task) => (
                <div key={task.id}>
                    <div>
                        <div>
                            <h3>
                                {task.title}
                            </h3>
                            {task.description && (
                                <p>
                                    {task.description}
                                </p>
                            )}
                        </div>
                        <button
                            onClick={() => onToggleComplete(task.id)}
                        >
                            {task.completed ? 'Undo' : 'Done'}
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default TaskList;