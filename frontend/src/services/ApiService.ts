import axios, { type AxiosResponse } from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config: any) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: any) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response: any) => response,
  (error: any) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  register: (userData: { email: string; fullName: string; password: string }): Promise<AxiosResponse> => {
    return apiClient.post('/auth/register', userData);
  },

  login: (credentials: { email: string; password: string }): Promise<AxiosResponse> => {
    return apiClient.post('/auth/login', credentials);
  },
};
export const projectService = {
  getAll: (): Promise<AxiosResponse> => {
    return apiClient.get('/projects');
  },

  getById: (projectId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/projects/${projectId}`);
  },

  create: (projectData: { name: string; description: string }): Promise<AxiosResponse> => {
    return apiClient.post('/projects', projectData);
  },

  update: (projectId: number, projectData: { name: string; description: string; isArchived?: boolean }): Promise<AxiosResponse> => {
    return apiClient.put(`/projects/${projectId}`, projectData);
  },

  archive: (projectId: number): Promise<AxiosResponse> => {
    return apiClient.delete(`/projects/${projectId}`);
  },

  addMember: (projectId: number, memberId: number): Promise<AxiosResponse> => {
    return apiClient.post(`/projects/${projectId}/members`, { userId: memberId });
  },

  createSprint: (projectId: number, sprintData: { name: string; startDate: string; endDate: string }): Promise<AxiosResponse> => {
    return apiClient.post(`/projects/${projectId}/sprints`, sprintData);
  },

  getKanban: (projectId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/projects/${projectId}/kanban`);
  },
};

export const taskService = {
  getAll: (): Promise<AxiosResponse> => {
    return apiClient.get('/tasks');
  },

  getById: (taskId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/tasks/${taskId}`);
  },

  create: (taskData: { projectId: number; sprintId: number | null; title: string; description: string; priorityId: number }): Promise<AxiosResponse> => {
    return apiClient.post('/tasks', taskData);
  },

  update: (taskId: number, taskData: { title: string; description: string; priorityId: number; sprintId: number }): Promise<AxiosResponse> => {
    return apiClient.put(`/tasks/${taskId}`, taskData);
  },

  delete: (taskId: number): Promise<AxiosResponse> => {
    return apiClient.delete(`/tasks/${taskId}`);
  },

  assign: (taskId: number, userId: number): Promise<AxiosResponse> => {
    return apiClient.post(`/tasks/${taskId}/assignee`, { userId });
  },

  removeAssignee: (taskId: number): Promise<AxiosResponse> => {
    return apiClient.delete(`/tasks/${taskId}/assignee`);
  },

  changeStatus: (taskId: number, statusId: number): Promise<AxiosResponse> => {
    return apiClient.put(`/tasks/${taskId}/status`, { statusId });
  },

  getComments: (taskId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/tasks/${taskId}/comments`);
  },

  addComment: (taskId: number, commentData: { userId: number; content: string }): Promise<AxiosResponse> => {
    return apiClient.post(`/tasks/${taskId}/comments`, commentData);
  },

  updateComment: (taskId: number, commentId: number, commentData: { content: string }): Promise<AxiosResponse> => {
    return apiClient.put(`/tasks/${taskId}/comments/${commentId}`, commentData);
  },

  deleteComment: (taskId: number, commentId: number): Promise<AxiosResponse> => {
    return apiClient.delete(`/tasks/${taskId}/comments/${commentId}`);
  },

  getPriorities: (): Promise<AxiosResponse> => {
    return apiClient.get('/tasks/priorities');
  },

  syncToYandexTracker: (taskId: number): Promise<AxiosResponse> => {
    return apiClient.post(`/tasks/${taskId}/sync`);
  },
};

export const userService = {
  getAll: (): Promise<AxiosResponse> => {
    return apiClient.get('/users');
  },

  getById: (userId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/users/${userId}`);
  },

  update: (userId: number, userData: { fullName: string }): Promise<AxiosResponse> => {
    return apiClient.put(`/users/${userId}`, userData);
  },

  delete: (userId: number): Promise<AxiosResponse> => {
    return apiClient.delete(`/users/${userId}`);
  },

  getRoles: (userId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/users/${userId}/roles`);
  },

  assignRole: (userId: number, roleId: number): Promise<AxiosResponse> => {
    return apiClient.post(`/users/${userId}/roles`, { roleId });
  },
};

export const roleService = {
  getAll: (): Promise<AxiosResponse> => {
    return apiClient.get('/roles');
  },
};

export const sprintService = {
  getById: (sprintId: number): Promise<AxiosResponse> => {
    return apiClient.get(`/sprints/${sprintId}`);
  },

  update: (sprintId: number, sprintData: { name: string; startDate: string; endDate: string }): Promise<AxiosResponse> => {
    return apiClient.put(`/sprints/${sprintId}`, sprintData);
  },

  delete: (sprintId: number): Promise<AxiosResponse> => {
    return apiClient.delete(`/sprints/${sprintId}`);
  },
};

export default apiClient;