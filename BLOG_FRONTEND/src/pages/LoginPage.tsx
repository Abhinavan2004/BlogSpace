import { useAuth } from '../components/AuthContext';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const { loginWithEmail, loginWithGoogle, isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) navigate('/');
  }, [isAuthenticated]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-gray-500">Loading...</p>
      </div>
    );
  }

  return (
<div className="flex-1 flex items-center justify-center bg-gray-50">
          <div className="max-w-md w-full space-y-6 p-8 bg-white rounded-xl shadow-md">
        <div className="text-center">
          <h2 className="text-3xl font-extrabold text-gray-900">Welcome to BlogSpace</h2>
          <p className="mt-2 text-sm text-gray-600">Sign in or create an account</p>
        </div>

        <div className="space-y-3">
          {/* Google Login */}
          <button
            onClick={loginWithGoogle}
            className="w-full flex items-center justify-center gap-3 py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 transition"
          >
            <img
              src="https://www.svgrepo.com/show/475656/google-color.svg"
              alt="Google"
              className="w-5 h-5"
            />
            Continue with Google
          </button>

          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-gray-200" />
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="px-2 bg-white text-gray-400">or</span>
            </div>
          </div>

          {/* Email/Password Login */}
          <button
            onClick={loginWithEmail}
            className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition"
          >
            Continue with Email
          </button>
        </div>

        <p className="text-center text-xs text-gray-400">
          New users are automatically registered on first sign in.
        </p>
      </div>
    </div>
  );
};

export default LoginPage;