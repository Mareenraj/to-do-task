import React, { useState } from 'react';

interface TaskFormProps {
    onAdd: (title: string, description: string) => void;
}

const TaskForm: React.FC<TaskFormProps> = ({ onAdd }) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onAdd(title, description);
        setTitle('');
        setDescription('');
    };

    return (
        <div>
            <h2>Add a Task</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">
                        Title
                    </label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Enter task title"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="description">
                        Description
                    </label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Enter task description (optional)"
                    />
                </div>
                <button type="submit">
                    Add
                </button>
            </form>
        </div>
    );
};

export default TaskForm;