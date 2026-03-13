import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../components/AuthContext';  // ← correct import

const OAuth2CallbackPage = () => {
  const navigate = useNavigate();
  const { setTokenAndSync } = useAuth();  // ← destructure from the hook

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');

    if (token) {
      setTokenAndSync(token)
        .then(() => navigate('/', { replace: true }))
        .catch(() => navigate('/login', { replace: true }));
    } else {
      navigate('/login', { replace: true });
    }
  }, []);

  return (
    <div className="min-h-screen flex items-center justify-center">
      <p className="text-gray-500">Signing you in...</p>
    </div>
  );
};

export default OAuth2CallbackPage;