import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { taskService, userService, projectService } from '../services/ApiService';
import './PageStyles.css';

interface User {
  id: number;
  fullName: string;
  email: string;
}

interface Project {
  id: number;
  name: string;
  description: string;
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

interface Comment {
  id: number;
  taskId: number;
  userId: number;
  content: string;
  authorName: string;
  createdAt: string;
}

const TaskDetail: React.FC = () => {
  const { taskId } = useParams<{ taskId: string }>();
  
  if (!taskId) {
    return <div className="error">Task ID is required</div>;
  }
  
  const taskIdNum = parseInt(taskId);
  if (isNaN(taskIdNum)) {
    return <div className="error">Invalid Task ID</div>;
  }
  
  if (taskIdNum <= 0) {
    return <div className="loading">Invalid task ID</div>;
  }
  const navigate = useNavigate();
  const { token } = useAuth();
  const [task, setTask] = useState<Task | null>(null);
  const [project, setProject] = useState<Project | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [newComment, setNewComment] = useState('');
  const [showCommentForm, setShowCommentForm] = useState(false);

  useEffect(() => {
    if (token && taskId) {
      fetchTaskDetails();
    }
  }, [token, taskId]);

  const fetchTaskDetails = async () => {
    try {
      setLoading(true);
      
      const [taskRes, usersRes] = await Promise.all([
        taskService.getById(taskIdNum),
        userService.getAll()
      ]);
      
      const taskData = taskRes.data as Task;
      setTask(taskData);
      
      if (taskData.projectId) {
        try {
          const projectRes = await projectService.getById(taskData.projectId);
          setProject(projectRes.data);
        } catch (err) {
          console.error('Failed to fetch project:', err);
        }
      }
      
      try {
        const commentsRes = await taskService.getComments(taskIdNum);
        setComments(commentsRes.data);
      } catch (err) {
        console.error('Failed to fetch comments:', err);
      }
      
      setUsers(usersRes.data);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to fetch task details');
    } finally {
      setLoading(false);
    }
  };

  const handleAddComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment.trim() || !task) return;
    
    try {

      const currentUser = users[0]; 
      if (!currentUser) {
        setError('No users available to add comment');
        return;
      }
      
      await taskService.addComment(taskIdNum, {
        userId: currentUser.id,
        content: newComment
      });
      
      setNewComment('');
      setShowCommentForm(false);

      const commentsRes = await taskService.getComments(taskIdNum);
      setComments(commentsRes.data);
    } catch (err: any) {
      setError(err.message || 'Failed to add comment');
    }
  };

  const handleDeleteComment = async (commentId: number) => {
    if (window.confirm('Are you sure you want to delete this comment?')) {
      try {
        await taskService.deleteComment(taskIdNum, commentId);

        const commentsRes = await taskService.getComments(taskIdNum);
        setComments(commentsRes.data);
      } catch (err: any) {
        setError(err.message || 'Failed to delete comment');
      }
    }
  };

  const handleSyncToYandexTracker = async () => {
    try {
      await taskService.syncToYandexTracker(taskIdNum);
      alert('Task synced to Yandex Tracker successfully!');
    } catch (err: any) {
      setError(err.message || 'Failed to sync task to Yandex Tracker');
    }
  };

  if (loading) return (
    <div className="loading">
      <div className="spinner"></div>
      Loading task details...
    </div>
  );
  if (error) return <div className="error">Error: {error}</div>;
  if (!task) return <div className="error">Task not found</div>;

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Task Details</h1>
        <div className="task-detail-actions">
          <button 
            onClick={() => navigate(-1)}
            className="btn-secondary"
          >
            Back to Tasks
          </button>
        </div>
      </div>

      <div className="task-detail-content">
        <div className="task-detail-card">
          <div className="task-detail-header">
            <h2>{task.title}</h2>
            <div className="task-detail-meta">
              <span className={`status-badge status-${task.status.toLowerCase().replace(/\s+/g, '-')}`}>
                {task.status}
              </span>
              <span className={`priority-badge priority-${task.priority.toLowerCase()}`}>
                {task.priority} Priority
              </span>
            </div>
          </div>

          <div className="task-detail-body">
            <div className="task-detail-section">
              <h3>Description</h3>
              <p>{task.description || 'No description provided.'}</p>
            </div>

            <div className="task-detail-info">
              <div className="info-item">
                <strong>Project:</strong>
                <span>{project?.name || 'Loading...'}</span>
              </div>
              
              <div className="info-item">
                <strong>Assignee:</strong>
                <span>{task.assignee ? task.assignee.fullName : 'Unassigned'}</span>
              </div>
              
              <div className="info-item">
                <strong>Created:</strong>
                <span>{new Date(task.createdAt).toLocaleDateString()}</span>
              </div>
              
              <div className="info-item">
                <strong>Last Updated:</strong>
                <span>{new Date(task.updatedAt).toLocaleDateString()}</span>
              </div>
            </div>

            <div className="task-detail-actions">
              <button className="btn-secondary">Edit Task</button>
              <button className="btn-secondary">Change Status</button>
              <button 
                onClick={handleSyncToYandexTracker}
                className="btn-primary"
              >
                Sync to Yandex
              </button>
              <button className="btn-danger">Delete Task</button>
            </div>
          </div>
        </div>

        {/* Comments Section */}
        <div className="comments-section">
          <div className="comments-header">
            <h3>Comments ({comments.length})</h3>
            <button 
              onClick={() => setShowCommentForm(!showCommentForm)}
              className="btn-primary"
            >
              {showCommentForm ? 'Cancel' : 'Add Comment'}
            </button>
          </div>

          {showCommentForm && (
            <form onSubmit={handleAddComment} className="comment-form">
              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                placeholder="Write a comment..."
                rows={4}
                required
              />
              <button type="submit" className="btn-primary">Post Comment</button>
            </form>
          )}

          <div className="comments-list">
            {comments.length > 0 ? (
              comments.map((comment) => (
                <div key={comment.id} className="comment-item">
                  <div className="comment-header">
                    <strong>{comment.authorName}</strong>
                    <small>{new Date(comment.createdAt).toLocaleString()}</small>
                  </div>
                  <div className="comment-content">
                    {comment.content}
                  </div>
                  <div className="comment-actions">
                    <button 
                      onClick={() => handleDeleteComment(comment.id)}
                      className="btn-danger btn-small"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))
            ) : (
              <p className="no-comments">No comments yet. Be the first to comment!</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TaskDetail;