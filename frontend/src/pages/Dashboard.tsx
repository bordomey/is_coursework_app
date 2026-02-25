import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './PageStyles.css';
import '../App.css'; 

const Dashboard: React.FC = () => {
  const { user } = useAuth();

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Welcome, {user?.fullName || 'User'}!</h1>
      </div>
      
      <div className="dashboard-content">
        <h2>Your Workspace</h2>
        
        <div className="quick-links">
          <Link to="/projects" className="quick-link-card">
            <h3>Projects</h3>
            <p>Manage your game development projects</p>
          </Link>
          
          <Link to="/tasks" className="quick-link-card">
            <h3>Tasks</h3>
            <p>View and manage your assigned tasks</p>
          </Link>
          
          <Link to="/projects" className="quick-link-card">
            <h3>Kanban Boards</h3>
            <p>Visualize your project workflows</p>
          </Link>
        </div>
        
        <div className="recent-activity">
          <h3>Recent Activity</h3>
          <div className="loading">
            <div className="spinner"></div>
            Coming soon...
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;