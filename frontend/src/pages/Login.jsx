import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login, register } from '../api';

export default function Login() {
  const nav = useNavigate();
  const [mode, setMode] = useState('login');
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');
  const [err, setErr] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    setErr('');
    try {
      if (mode === 'register') {
        await register(user, pass);
        // After register, auto-login to get token
        const loginRes = await login(user, pass);
        localStorage.setItem('token', loginRes.token);
        localStorage.setItem('username', loginRes.username);
      } else {
        const res = await login(user, pass);
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', res.username);
      }
      nav('/artifacts');
    } catch (e) {
      setErr(e.response?.data?.message || 'Error');
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '80px auto', padding: 24 }}>
      <h2 style={{ marginBottom: 24 }}>{mode === 'login' ? 'Login' : 'Register'}</h2>
      {err && <p style={{ color: '#b91c1c', background: '#fff5f5', padding: 10, borderRadius: 6 }}>{err}</p>}
      <form onSubmit={submit}>
        <input value={user} onChange={e => setUser(e.target.value)}
          placeholder="Username" required style={inputStyle} />
        <input value={pass} onChange={e => setPass(e.target.value)}
          type="password" placeholder="Password" required style={inputStyle} />
        <button type="submit" style={{
          width: '100%', padding: 10, background: '#0f766e', color: '#fff',
          border: 'none', borderRadius: 6, fontSize: 16, cursor: 'pointer'
        }}>{mode === 'login' ? 'Login' : 'Register'}</button>
      </form>
      <p style={{ textAlign: 'center', marginTop: 16, color: '#666' }}>
        <button onClick={() => setMode(m => m === 'login' ? 'register' : 'login')}
          style={{ border: 'none', background: 'none', color: '#0f766e', cursor: 'pointer', fontSize: 14 }}>
          {mode === 'login' ? 'Create an account' : 'Back to login'}
        </button>
      </p>
    </div>
  );
}

const inputStyle = {
  display: 'block', width: '100%', padding: '10px 12px',
  border: '1px solid #c9c6bd', borderRadius: 6, fontSize: 15,
  marginBottom: 12, boxSizing: 'border-box',
};
