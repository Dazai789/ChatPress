import { useState, useEffect } from 'react';
import axios from 'axios';

export default function Logs() {
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    const token = localStorage.getItem('token');
    axios.get('/admin/logs', {
      params: { page, size: 20 },
      headers: { Authorization: `Bearer ${token}` }
    }).then(() => {
      // Admin logs endpoint returns HTML — we display as raw for now
      setItems([{ info: 'Operation logs are available at /admin/logs as an HTML page' }]);
    }).catch(() => {
      setItems([{ info: 'Operation logs: access /admin/logs in browser' }]);
    });
  }, [page]);

  return (
    <div>
      <h2 style={{ marginBottom: 24 }}>Operation Logs</h2>
      <p style={{ color: '#666', marginBottom: 20 }}>
        Operation logs are recorded automatically via AOP. Each create/edit/delete/import action is logged with username, action type, target, duration, and timestamp.
      </p>
      <p style={{ color: '#666' }}>
        <a href="/admin/logs" target="_blank" style={{ color: '#0f766e' }}>View full logs page (server-rendered HTML)</a>
      </p>
    </div>
  );
}
