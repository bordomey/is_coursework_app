import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { projectService, taskService } from '../services/ApiService';
import './PageStyles.css';

interface User {
  id: number;
  fullName: string;
}

interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  statusID: number;
  priority: string;
  assignee: User | null;
  projectId: number;
  sprintId: number | null;
  createdAt: string;
  updatedAt: string;
}
const ALL_STATUSES = [
  { id: 1, name: 'To Do' },
  { id: 2, name: 'In Progress' },
  { id: 3, name: 'Done' }
];
interface Project {
  id: number;
  name: string;
  description: string;
}

interface KanbanColumn {
  id: number;     // statusId
  name: string;   // status name
  tasks: Task[];
}

interface KanbanData {
  project: Project;
  columns: KanbanColumn[];
}

const Kanban: React.FC = () => {
  const { projectId } = useParams<{ projectId: string }>();
  const { token } = useAuth();

  const [kanbanData, setKanbanData] = useState<KanbanData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [dragOverColumn, setDragOverColumn] = useState<number | null>(null);

  const [showTaskForm, setShowTaskForm] = useState(false);
  const [newTask, setNewTask] = useState({
    title: '',
    description: '',
    priorityId: 1
  });
  const [priorities, setPriorities] = useState<{ id: number; name: string }[]>([]);

const fetchKanbanData = async () => {
  try {
    setLoading(true);
    const response = await projectService.getKanban(parseInt(projectId || '0'));

    const tasks: Task[] = response.data.tasks;
    const statuses = ALL_STATUSES;

    const columns: KanbanColumn[] = statuses.map((status: any) => ({
      id: status.id,
      name: status.name,
      tasks: []
    }));

    tasks.forEach(task => {
      const column = columns.find(c => c.name === task.status);
      if (column) {
        column.tasks.push(task);
      }
    });

    setKanbanData({
      project: {
        id: parseInt(projectId || '0'),
        name: 'Project',
        description: 'Project Description'
      },
      columns
    });

    setError('');
  } catch (err: any) {
    setError(err.message || 'Failed to fetch kanban data');
  } finally {
    setLoading(false);
  }
};

  const fetchPriorities = async () => {
    try {
      const response = await taskService.getPriorities();
      setPriorities(response.data);
      if (response.data.length > 0) {
        setNewTask(prev => ({ ...prev, priorityId: response.data[0].id }));
      }
    } catch (err) {
      console.error('Failed to fetch priorities');
    }
  };

  useEffect(() => {
    if (token && projectId) {
      fetchPriorities();
      fetchKanbanData();
    }
  }, [token, projectId]);


const handleDragStart = (e: React.DragEvent, task: Task) => {
  e.dataTransfer.setData('application/json', JSON.stringify({
    taskId: task.id,
    sourceStatusId: task.statusID
  }));
  e.dataTransfer.effectAllowed = 'move';
};

  const handleDrop = async (e: React.DragEvent, targetStatusId: number) => {
  e.preventDefault();

  const raw = e.dataTransfer.getData('application/json');
  if (!raw) return;

  const { taskId, sourceStatusId } = JSON.parse(raw);

  if (!taskId || !targetStatusId) { console.log("Invalid task or status ID"); return; }
  if (sourceStatusId === targetStatusId) return;

  try {
    await taskService.changeStatus(taskId, targetStatusId);
  } catch {
    fetchKanbanData();
  }
};

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await taskService.create({
        title: newTask.title,
        description: newTask.description,
        projectId: parseInt(projectId || '0'),
        priorityId: newTask.priorityId,
        sprintId: null
      });

      setNewTask({ title: '', description: '', priorityId: 1 });
      setShowTaskForm(false);
      fetchKanbanData();
    } catch (err: any) {
      setError(err.message || 'Failed to create task');
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

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!kanbanData) return null;

  return (
    <div className="page-container">
      <h1>Kanban Board</h1>

      <button onClick={() => setShowTaskForm(!showTaskForm)}>
        {showTaskForm ? 'Cancel' : '+ New Task'}
      </button>

      {showTaskForm && (
        <form onSubmit={handleCreateTask}>
          <input
            value={newTask.title}
            onChange={e => setNewTask({ ...newTask, title: e.target.value })}
            placeholder="Title"
            required
          />
          <textarea
            value={newTask.description}
            onChange={e => setNewTask({ ...newTask, description: e.target.value })}
          />
          <select
            value={newTask.priorityId}
            onChange={e => setNewTask({ ...newTask, priorityId: Number(e.target.value) })}
          >
            {priorities.map(p => (
              <option key={p.id} value={p.id}>{p.name}</option>
            ))}
          </select>
          <button type="submit">Create</button>
        </form>
      )}

      <div className="kanban-board">
        {kanbanData.columns.map(column => (
          <div
            key={column.id}
            className={`kanban-column ${dragOverColumn === column.id ? 'drag-over' : ''}`}
            onDragOver={e => {
              e.preventDefault();
              setDragOverColumn(column.id);
            }}
            onDragLeave={() => setDragOverColumn(null)}
            onDrop={e => {
              setDragOverColumn(null);
              handleDrop(e, column.id);
            }}
          >
            <h3>{column.name} ({column.tasks.length})</h3>

            {column.tasks.map(task => (
              <div
                key={task.id}
                draggable
                className="kanban-task"
                onDragStart={e => handleDragStart(e, task)}
              >
                <div>{task.title}</div>
                <small>{task.priority}</small>
                <div className="task-actions">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleSyncToYandexTracker(task.id);
                    }}
                    className="btn-secondary btn-small">
                    Sync to Yandex
                  </button>
                </div>
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Kanban;