import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { createArtifact, updateArtifact, getArtifact } from '../api';

export default function Form() {
  const nav = useNavigate();
  const { id } = useParams();
  const isEdit = !!id;

  const [title, setTitle] = useState('');
  const [sourceContent, setSourceContent] = useState('');
  const [tags, setTags] = useState('');
  const [err, setErr] = useState('');

  useEffect(() => {
    if (isEdit) {
      getArtifact(id).then(a => {
        setTitle(a.title);
        setSourceContent(a.sourceContent);
        setTags((a.tags || []).join(', '));
      }).catch(() => setErr('Artifact not found'));
    }
  }, [id]);

  const submit = async (e) => {
    e.preventDefault();
    setErr('');
    const tagList = tags.split(',').map(t => t.trim()).filter(Boolean);
    try {
      if (isEdit) {
        await updateArtifact(id, { title, sourceContent, tags: tagList });
      } else {
        await createArtifact({ title, sourceContent, tags: tagList });
      }
      nav('/artifacts');
    } catch (e) {
      setErr(e.response?.data?.message || 'Error');
    }
  };

  return (
    <div style={{ maxWidth: 900 }}>
      <h2 style={{ marginBottom: 24 }}>{isEdit ? 'Edit Artifact' : 'New Artifact'}</h2>
      {err && <p style={{ color: '#b91c1c', background: '#fff5f5', padding: 10, borderRadius: 6 }}>{err}</p>}
      <form onSubmit={submit}>
        <div style={{ marginBottom: 16 }}>
          <label style={labelStyle}>Title</label>
          <input value={title} onChange={e => setTitle(e.target.value)}
            required maxLength={200} style={inputStyle} />
        </div>
        <div style={{ marginBottom: 16 }}>
          <label style={labelStyle}>Markdown</label>
          <textarea value={sourceContent} onChange={e => setSourceContent(e.target.value)}
            required rows={20} style={{ ...inputStyle, fontFamily: 'monospace', resize: 'vertical' }} />
        </div>
        <div style={{ marginBottom: 24 }}>
          <label style={labelStyle}>Tags (comma-separated)</label>
          <input value={tags} onChange={e => setTags(e.target.value)}
            placeholder="java, spring, tutorial" style={inputStyle} />
        </div>
        <div style={{ display: 'flex', gap: 12 }}>
          <button type="submit" style={btnStyle}>Save</button>
          <button type="button" onClick={() => nav(-1)} style={{ ...btnStyle, background: '#fff', color: '#333', border: '1px solid #c9c6bd' }}>Cancel</button>
        </div>
      </form>
    </div>
  );
}

const labelStyle = { display: 'block', marginBottom: 6, fontWeight: 650, color: '#404040', fontSize: 14 };
const inputStyle = { display: 'block', width: '100%', padding: '10px 12px', border: '1px solid #c9c6bd', borderRadius: 6, fontSize: 14, boxSizing: 'border-box' };
const btnStyle = { padding: '10px 20px', background: '#0f766e', color: '#fff', border: 'none', borderRadius: 6, fontSize: 15, cursor: 'pointer' };
