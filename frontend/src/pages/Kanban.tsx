import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { projectService } from '../services/ApiService';
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
  priority: string;
  assignee: User | null;
}

interface KanbanColumn {
  id: number;
  name: string;
  tasks: Task[];
}

interface Project {
  id: number;
  name: string;
  description: string;
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

  useEffect(() => {
    if (token && projectId) {
      fetchKanbanData();
    }
  }, [token, projectId]);

  const fetchKanbanData = async () => {
    try {
      setLoading(true);
      const response = await projectService.getKanban(parseInt(projectId || '0'));
      setKanbanData(response.data);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to fetch kanban data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading kanban board...</div>;
  if (error) return <div className="error">Error: {error}</div>;
  if (!kanbanData) return <div className="error">No kanban data found</div>;

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Kanban Board: {kanbanData.project.name}</h1>
        <button 
          onClick={() => window.location.href = `/projects`}
          className="btn-secondary"
        >
          Back to Projects
        </button>
      </div>
      
      <p>{kanbanData.project.description}</p>
      
      <div className="kanban-board">
        {kanbanData.columns.map(column => (
          <div key={column.id} className="kanban-column">
            <div className="kanban-column-header">
              <h3 className="column-title">{column.name}</h3>
              <span className="column-count">{column.tasks.length}</span>
            </div>
            <div className="kanban-tasks">
              {column.tasks.map(task => (
                <div key={task.id} className="kanban-task">
                  <div className="kanban-task-title">{task.title}</div>
                  <div className="kanban-task-info">
                    {task.assignee && <span>Assignee: {task.assignee.fullName}</span>}
                    <span>Priority: {task.priority}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Kanban;