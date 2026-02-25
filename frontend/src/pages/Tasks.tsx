import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { taskService, projectService, userService } from '../services/ApiService';
import { Link } from 'react-router-dom';
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

interface Priority {
  id: number;
  name: string;
}

const Tasks: React.FC = () => {
  const { token } = useAuth();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [projects, setProjects] = useState<Project[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [priorities, setPriorities] = useState<Priority[]>([]);
  const [newTask, setNewTask] = useState({
    title: '',
    description: '',
    projectId: 0,
    priorityId: 1,
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
      const [tasksRes, projectsRes, usersRes, prioritiesRes] = await Promise.all([
        taskService.getAll(),
        projectService.getAll(),
        userService.getAll(),
        taskService.getPriorities()
      ]);
      
      setTasks(tasksRes.data);
      setProjects(projectsRes.data);
      setUsers(usersRes.data);
      setPriorities(prioritiesRes.data);
      if (prioritiesRes.data && prioritiesRes.data.length > 0) {
        setNewTask(prev => ({ ...prev, priorityId: prioritiesRes.data[0].id }));
      }
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
        sprintId: newTask.sprintId 
      });
      setNewTask({
        title: '',
        description: '',
        projectId: 0,
        priorityId: 1,
        sprintId: null
      });
      setShowForm(false);
      fetchData(); 
    } catch (err: any) {
      setError(err.message || 'Failed to create task');
    }
  };

  const handleDeleteTask = async (taskId: number) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await taskService.delete(taskId);
        fetchData(); 
      } catch (err: any) {
        setError(err.message || 'Failed to delete task');
      }
    }
  };

  const handleSyncToYandexTracker = async (taskId: number) => {
    try {
      await taskService.syncToYandexTracker(taskId);
      alert('Task synced to Yandex Tracker successfully!');
    } catch (err: any) {
      setError(err.message || 'Failed to sync task to Yandex Tracker');
    }
  };

  const handleAssignTask = async (taskId: number, userId: number) => {
    try {
      await taskService.assign(taskId, userId);
      fetchData(); 
    } catch (err: any) {
      setError(err.message || 'Failed to assign task');
    }
  };

  const handleChangeStatus = async (taskId: number, statusId: number) => {
    try {
      await taskService.changeStatus(taskId, statusId);
      fetchData(); 
    } catch (err: any) {
      setError(err.message || 'Failed to change task status');
    }
  };

  if (loading) return (
    <div className="loading">
      <div className="spinner"></div>
      Loading tasks...
    </div>
  );
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
                {priorities.map(priority => (
                  <option key={priority.id} value={priority.id}>{priority.name}</option>
                ))}
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
              <div className="task-title">
                <Link to={`/task/${task.id}`} className="task-link">{task.title}</Link>
              </div>
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
                onClick={() => handleSyncToYandexTracker(task.id)}
                className="btn-primary"
              >
                Sync to Yandex
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