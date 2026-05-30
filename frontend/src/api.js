import axios from 'axios';

const api = axios.create({ baseURL: '/api' });

// Attach JWT token to every request
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Redirect to login on 401
api.interceptors.response.use(
  r => r,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth
export const login = (username, password) =>
  api.post('/auth/login', { username, password }).then(r => r.data);

export const register = (username, password) =>
  api.post('/auth/register', { username, password }).then(r => r.data);

// Artifacts
export const listArtifacts = (params) =>
  api.get('/artifacts', { params }).then(r => r.data);

export const getArtifact = (id) =>
  api.get(`/artifacts/${id}`).then(r => r.data);

export const createArtifact = (data) =>
  api.post('/artifacts', data).then(r => r.data);

export const updateArtifact = (id, data) =>
  api.put(`/artifacts/${id}`, data).then(r => r.data);

export const updateStatus = (id, status) =>
  api.put(`/artifacts/${id}/status`, { status }).then(r => r.data);

export const deleteArtifact = (id) =>
  api.delete(`/artifacts/${id}`).then(() => null);
