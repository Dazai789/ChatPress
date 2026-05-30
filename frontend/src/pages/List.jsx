import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { listArtifacts, deleteArtifact, updateStatus } from '../api';

export default function List() {
  const [data, setData] = useState({ items: [], page: 0, totalPages: 1, totalItems: 0 });
  const [page, setPage] = useState(0);
  const [q, setQ] = useState('');
  const [status, setStatus] = useState('');
  const [tag, setTag] = useState('');

  const load = (pageNum) => {
    const p = pageNum ?? page;
    listArtifacts({ page: p, size: 10, q: q || undefined, status: status || undefined, tag: tag || undefined })
      .then(setData)
      .catch(console.error);
  };

  useEffect(() => load(page), [page]);

  const del = async (id) => {
    if (!confirm('Delete this artifact?')) return;
    await deleteArtifact(id);
    load(page);
  };

  const toggle = async (id, st) => {
    await updateStatus(id, st === 'published' ? 'draft' : 'published');
    load(page);
  };

  return (
    <div>
      {/* Search bar */}
      <div style={{ display: 'flex', gap: 10, marginBottom: 20 }}>
        <input value={q} onChange={e => setQ(e.target.value)}
          placeholder="Search title + content" style={inputStyle} />
        <input value={tag} onChange={e => setTag(e.target.value)}
          placeholder="Tag" style={{ ...inputStyle, width: 140 }} />
        <select value={status} onChange={e => setStatus(e.target.value)}
          style={{ ...inputStyle, width: 140 }}>
          <option value="">All</option>
          <option value="published">Published</option>
          <option value="draft">Draft</option>
        </select>
        <button onClick={() => page === 0 ? load(0) : setPage(0)} style={btnPrimary}>Search</button>
      </div>

      {/* Pager info */}
      <p style={{ color: '#666', marginBottom: 12 }}>
        {data.totalItems} articles · Page {data.page + 1} of {data.totalPages || 1}
      </p>

      {/* Table */}
      <table style={{ width: '100%', borderCollapse: 'collapse', background: '#fff', border: '1px solid #dedbd2' }}>
        <thead>
          <tr style={{ background: '#eeece4', textAlign: 'left' }}>
            <th style={thStyle}>Title</th>
            <th style={thStyle}>Tags</th>
            <th style={thStyle}>Status</th>
            <th style={thStyle}>Slug</th>
            <th style={thStyle}>Created</th>
            <th style={thStyle}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.items.map(a => (
            <tr key={a.id} style={{ borderBottom: '1px solid #ebe8df' }}>
              <td style={tdStyle}>
                <Link to={`/artifacts/${a.id}`} style={{ color: '#0f766e', fontWeight: 600, textDecoration: 'none' }}>
                  {a.title}
                </Link>
              </td>
              <td style={tdStyle}>
                {(a.tags || []).map(t => (
                  <span key={t} style={tagStyle}>{t}</span>
                ))}
              </td>
              <td style={tdStyle}>
                <span style={{
                  display: 'inline-block', padding: '2px 8px', borderRadius: 999,
                  background: a.status === 'published' ? '#e7eee9' : '#eee7db',
                  color: a.status === 'published' ? '#166534' : '#92400e',
                  fontSize: 13
                }}>{a.status}</span>
              </td>
              <td style={{ ...tdStyle, fontFamily: 'monospace', fontSize: 13 }}>{a.slug}</td>
              <td style={{ ...tdStyle, fontSize: 13, color: '#666' }}>
                {a.createdAt?.substring(0, 10)}
              </td>
              <td style={tdStyle}>
                <Link to={`/artifacts/${a.id}/edit`} style={actLink}>Edit</Link>
                <button onClick={() => toggle(a.id, a.status)} style={actBtn}>
                  {a.status === 'published' ? 'Draft' : 'Publish'}
                </button>
                <button onClick={() => del(a.id)} style={{ ...actBtn, color: '#b91c1c' }}>Del</button>
              </td>
            </tr>
          ))}
          {data.items.length === 0 && (
            <tr><td colSpan={6} style={{ ...tdStyle, textAlign: 'center', color: '#999' }}>No articles found</td></tr>
          )}
        </tbody>
      </table>

      {/* Pager */}
      {data.totalPages > 1 && (
        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 16 }}>
          <button onClick={() => setPage(p => p - 1)} disabled={page === 0} style={btnSecondary}>&larr; Previous</button>
          <button onClick={() => setPage(p => p + 1)} disabled={page >= data.totalPages - 1} style={btnSecondary}>Next &rarr;</button>
        </div>
      )}
    </div>
  );
}

const inputStyle = { padding: '8px 12px', border: '1px solid #c9c6bd', borderRadius: 6, fontSize: 14, flex: 1 };
const thStyle = { padding: '10px 14px', fontSize: 13, fontWeight: 650, textTransform: 'uppercase', color: '#4a4a4a' };
const tdStyle = { padding: '10px 14px', fontSize: 14, verticalAlign: 'middle' };
const tagStyle = { display: 'inline-block', padding: '2px 7px', borderRadius: 999, background: '#e0f2f1', color: '#0f766e', fontSize: 12, marginRight: 4 };
const btnPrimary = { padding: '8px 16px', background: '#0f766e', color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 14 };
const btnSecondary = { padding: '8px 16px', border: '1px solid #c9c6bd', borderRadius: 6, background: '#fff', cursor: 'pointer', fontSize: 14 };
const actLink = { color: '#0f766e', textDecoration: 'none', marginRight: 10, fontSize: 13 };
const actBtn = { border: 'none', background: 'none', color: '#0f766e', cursor: 'pointer', fontSize: 13, marginRight: 8 };
