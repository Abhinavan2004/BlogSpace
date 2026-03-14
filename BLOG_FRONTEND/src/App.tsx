import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import NavBar from "./components/NavBar";
import HomePage from "./pages/HomePage";
import EditPostPage from "./pages/EditPostPage";
import PostPage from "./pages/PostPage";
import CategoriesPage from "./pages/CategoriesPage";
import TagsPage from "./pages/TagsPage";
import DraftsPage from "./pages/DraftsPage";
import LoginPage from "./pages/LoginPage";
import OAuth2CallbackPage from './pages/OAuth2CallbackPage';
import { AuthProvider, useAuth } from "./components/AuthContext";
import EmailLoginPage from './pages/EmailLoginPage';
import RegisterPage from "./pages/RegisterPage";



// Protected Route component
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useAuth();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

function AppContent() {
  const { isAuthenticated, logout, user } = useAuth();

  return (
    <BrowserRouter>
      <NavBar 
        isAuthenticated={isAuthenticated}
        userProfile={user ? {
          name: user.name,
          avatar: undefined // Add avatar support if needed
        } : undefined}
        onLogout={logout}
      />
<main className="w-full max-w-full overflow-x-hidden px-4 py-6">
          <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route 
            path="/posts/new" 
            element={
              <ProtectedRoute>
                <EditPostPage />
              </ProtectedRoute>
            } 
          />
          <Route path="/oauth2/callback" element={<OAuth2CallbackPage />} />

          <Route path="/posts/:id" element={<PostPage isAuthenticated={isAuthenticated}/>} />
          <Route 
            path="/posts/:id/edit" 
            element={
              <ProtectedRoute>
                <EditPostPage />
              </ProtectedRoute>
            } 
          />
          <Route path="/register" element={<RegisterPage />} />

          <Route path="/login/email" element={<EmailLoginPage />} />
          <Route path="/categories" element={<CategoriesPage isAuthenticated={isAuthenticated}/>} />
          <Route path="/tags" element={<TagsPage isAuthenticated={isAuthenticated}/>} />
          <Route 
            path="/posts/drafts" 
            element={
              <ProtectedRoute>
                <DraftsPage />
              </ProtectedRoute>
            } 
          />
        </Routes>
      </main>
    
    </BrowserRouter>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;