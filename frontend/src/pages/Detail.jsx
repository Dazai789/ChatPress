import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { getArtifact, deleteArtifact } from '../api';

export default function Detail() {
  const { id } = useParams();
  const nav = useNavigate();
  const [a, setA] = useState(null);

  useEffect(() => {
    getArtifact(id).then(setA).catch(() => nav('/artifacts'));
  }, [id]);

  const del = async () => {
    if (!confirm('Delete this artifact?')) return;
    await deleteArtifact(id);
    nav('/artifacts');
  };

  if (!a) return <p>Loading...</p>;

  return (
    <div style={{ maxWidth: 900 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: 20 }}>
        <h2 style={{ margin: 0 }}>{a.title}</h2>
        <div style={{ display: 'flex', gap: 10 }}>
          <Link to={`/artifacts/${a.id}/edit`} style={btnStyle}>Edit</Link>
          <button onClick={del} style={{ ...btnStyle, background: '#b91c1c' }}>Delete</button>
        </div>
      </div>

      <div style={{ display: 'flex', gap: 8, marginBottom: 24 }}>
        <span style={statusChip(a.status)}>{a.status}</span>
        {(a.tags || []).map(t => (
          <span key={t} style={tagChip}>{t}</span>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '8px 16px', marginBottom: 24, padding: 16, background: '#fff', border: '1px solid #dedbd2', borderRadius: 6 }}>
        <dt style={{ fontWeight: 650, color: '#525252' }}>Slug</dt>
        <dd style={{ margin: 0, fontFamily: 'monospace' }}>{a.slug}</dd>
        <dt style={{ fontWeight: 650, color: '#525252' }}>Created</dt>
        <dd style={{ margin: 0 }}>{a.createdAt}</dd>
        <dt style={{ fontWeight: 650, color: '#525252' }}>Updated</dt>
        <dd style={{ margin: 0 }}>{a.updatedAt}</dd>
      </div>

      <h3 style={{ marginBottom: 12 }}>Markdown</h3>
      <pre style={{ padding: 16, background: '#fff', border: '1px solid #dedbd2', borderRadius: 6, whiteSpace: 'pre-wrap', fontFamily: 'monospace', lineHeight: 1.6, fontSize: 14 }}>{a.sourceContent}</pre>

      <h3 style={{ margin: '24px 0 12px' }}>Preview</h3>
      <div style={{ padding: 16, background: '#fff', border: '1px solid #dedbd2', borderRadius: 6 }}
        dangerouslySetInnerHTML={{ __html: a.renderedHtml }} />

      <p style={{ marginTop: 24 }}>
        <Link to="/artifacts" style={{ color: '#0f766e' }}>&larr; Back to list</Link>
        {' | '}
        <a href={`/p/${a.slug}`} target="_blank" style={{ color: '#0f766e' }}>Open public page</a>
      </p>
    </div>
  );
}

const btnStyle = { padding: '8px 16px', background: '#0f766e', color: '#fff', border: 'none', borderRadius: 6, fontSize: 14, cursor: 'pointer', textDecoration: 'none', display: 'inline-block' };
const statusChip = (s) => ({ display: 'inline-block', padding: '2px 8px', borderRadius: 999, background: s === 'published' ? '#e7eee9' : '#eee7db', color: s === 'published' ? '#166534' : '#92400e', fontSize: 13 });
const tagChip = { display: 'inline-block', padding: '2px 7px', borderRadius: 999, background: '#e0f2f1', color: '#0f766e', fontSize: 12 };
