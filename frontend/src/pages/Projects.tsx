import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { projectService } from '../services/ApiService';
import './PageStyles.css';

interface Project {
  id: number;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
  isArchived: boolean;
}

const Projects: React.FC = () => {
  const { token } = useAuth();
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [newProject, setNewProject] = useState({ name: '', description: '' });

  useEffect(() => {
    if (token) {
      fetchProjects();
    }
  }, [token]);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      const response = await projectService.getAll();
      setProjects(response.data);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to fetch projects');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateProject = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await projectService.create(newProject);
      setNewProject({ name: '', description: '' });
      setShowForm(false);
      fetchProjects(); // Refresh the list
    } catch (err: any) {
      setError(err.message || 'Failed to create project');
    }
  };

  const handleArchiveProject = async (projectId: number) => {
    try {
      await projectService.archive(projectId);
      fetchProjects(); // Refresh the list
    } catch (err: any) {
      setError(err.message || 'Failed to archive project');
    }
  };

  if (loading) return <div className="loading">Loading projects...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Projects</h1>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Cancel' : '+ New Project'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleCreateProject} className="form-container">
          <div className="form-group">
            <label htmlFor="projectName">Project Name:</label>
            <input
              type="text"
              id="projectName"
              value={newProject.name}
              onChange={(e) => setNewProject({ ...newProject, name: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="projectDescription">Description:</label>
            <textarea
              id="projectDescription"
              value={newProject.description}
              onChange={(e) => setNewProject({ ...newProject, description: e.target.value })}
            />
          </div>
          <button type="submit" className="btn-primary">Create Project</button>
        </form>
      )}

      <div className="projects-grid">
        {projects.map((project) => (
          <div key={project.id} className={`project-card ${project.isArchived ? 'archived' : ''}`}>
            <div className="project-header">
              <h3>{project.name}</h3>
              {project.isArchived && <span className="archived-badge">Archived</span>}
            </div>
            <p className="project-description">{project.description}</p>
            <div className="project-actions">
              <button 
                onClick={() => window.location.href = `/project/${project.id}`}
                className="btn-secondary"
              >
                View Details
              </button>
              {!project.isArchived && (
                <button 
                  onClick={() => handleArchiveProject(project.id)}
                  className="btn-danger"
                >
                  Archive
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {projects.length === 0 && (
        <div className="empty-state">
          <p>No projects found. Create your first project!</p>
        </div>
      )}
    </div>
  );
};

export default Projects;