import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { taskService, projectService, userService } from '../services/ApiService';
import './PageStyles.css';

interface User {
  id: number;
  fullName: string;
  email: string;
}

interface Project {
  id: number;
  name: string;
}

interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  priority: string;
  assignee: User | null;
  projectId: number;
  sprintId: number | null;
  createdAt: string;
  updatedAt: string;
}

const Tasks: React.FC = () => {
  const { token } = useAuth();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [projects, setProjects] = useState<Project[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [newTask, setNewTask] = useState({
    title: '',
    description: '',
    projectId: 0,
    priorityId: 1, // Default priority
    sprintId: null as number | null
  });

  useEffect(() => {
    if (token) {
      fetchData();
    }
  }, [token]);

  const fetchData = async () => {
    try {
      setLoading(true);
      // Fetch tasks, projects, and users in parallel
      const [tasksRes, projectsRes, usersRes] = await Promise.all([
        taskService.getAll(),
        projectService.getAll(),
        userService.getAll()
      ]);
      
      setTasks(tasksRes.data);
      setProjects(projectsRes.data);
      setUsers(usersRes.data);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await taskService.create({
        ...newTask,
        sprintId: newTask.sprintId || 0 // Convert null to 0 for backend compatibility
      });
      setNewTask({
        title: '',
        description: '',
        projectId: 0,
        priorityId: 1,
        sprintId: null
      });
      setShowForm(false);
      fetchData(); // Refresh the list
    } catch (err: any) {
      setError(err.message || 'Failed to create task');
    }
  };

  const handleDeleteTask = async (taskId: number) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await taskService.delete(taskId);
        fetchData(); // Refresh the list
      } catch (err: any) {
        setError(err.message || 'Failed to delete task');
      }
    }
  };

  const handleAssignTask = async (taskId: number, userId: number) => {
    try {
      await taskService.assign(taskId, userId);
      fetchData(); // Refresh the list
    } catch (err: any) {
      setError(err.message || 'Failed to assign task');
    }
  };

  const handleChangeStatus = async (taskId: number, statusId: number) => {
    try {
      await taskService.changeStatus(taskId, statusId);
      fetchData(); // Refresh the list
    } catch (err: any) {
      setError(err.message || 'Failed to change task status');
    }
  };

  if (loading) return <div className="loading">Loading tasks...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Tasks</h1>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Cancel' : '+ New Task'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleCreateTask} className="form-container">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="taskTitle">Task Title:</label>
              <input
                type="text"
                id="taskTitle"
                value={newTask.title}
                onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="taskProject">Project:</label>
              <select
                id="taskProject"
                value={newTask.projectId}
                onChange={(e) => setNewTask({ ...newTask, projectId: parseInt(e.target.value) })}
                required
              >
                <option value="">Select a project</option>
                {projects.map(project => (
                  <option key={project.id} value={project.id}>{project.name}</option>
                ))}
              </select>
            </div>
          </div>
          <div className="form-group">
            <label htmlFor="taskDescription">Description:</label>
            <textarea
              id="taskDescription"
              value={newTask.description}
              onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
            />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="taskPriority">Priority:</label>
              <select
                id="taskPriority"
                value={newTask.priorityId}
                onChange={(e) => setNewTask({ ...newTask, priorityId: parseInt(e.target.value) })}
              >
                <option value={1}>Low</option>
                <option value={2}>Medium</option>
                <option value={3}>High</option>
              </select>
            </div>
          </div>
          <button type="submit" className="btn-primary">Create Task</button>
        </form>
      )}

      <div className="tasks-container">
        {tasks.map((task) => (
          <div key={task.id} className="task-card">
            <div className="task-info">
              <div className="task-title">{task.title}</div>
              <div className="task-description">{task.description}</div>
              <div className="task-meta">
                <span>Project: {projects.find(p => p.id === task.projectId)?.name || 'N/A'}</span>
                <span>Priority: {task.priority}</span>
                <span>Status: {task.status}</span>
                {task.assignee && <span>Assigned to: {task.assignee.fullName}</span>}
              </div>
            </div>
            <div className="task-actions">
              <button 
                onClick={() => handleAssignTask(task.id, task.assignee?.id || users[0]?.id)}
                className="btn-secondary"
              >
                {task.assignee ? 'Reassign' : 'Assign'}
              </button>
              <button 
                onClick={() => handleChangeStatus(task.id, task.status === 'To Do' ? 2 : 1)}
                className="btn-secondary"
              >
                Change Status
              </button>
              <button 
                onClick={() => handleDeleteTask(task.id)}
                className="btn-danger"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>

      {tasks.length === 0 && (
        <div className="empty-state">
          <p>No tasks found. Create your first task!</p>
        </div>
      )}
    </div>
  );
};

export default Tasks;