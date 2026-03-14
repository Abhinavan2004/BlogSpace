import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const RegisterPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault(); // prevents browser default form submission
    setError("");
    setIsLoading(true);

    try {
      await axios.post("/api/v1/auth/register", {
        email,
        password,
        username,
      });

      navigate("/login/email");
    } catch (err: any) {
      console.error(err);
      setError(
        err?.response?.data?.message || "Registration failed. Please try again."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex-1 flex items-center justify-center">
      {/* autocomplete="off" stops browser from triggering its own auth dialog */}
      <form
        onSubmit={handleRegister}
        autoComplete="off"
        className="bg-white p-8 shadow rounded w-96"
      >
        <h2 className="text-xl font-bold mb-4">Create Account</h2>

        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded text-sm text-red-600">
            {error}
          </div>
        )}

        <input
          className="border w-full mb-3 p-2 rounded"
          placeholder="Username"
          value={username}
          autoComplete="username"
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <input
          type="email"
          className="border w-full mb-3 p-2 rounded"
          placeholder="Email"
          value={email}
          autoComplete="email"
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          className="border w-full mb-3 p-2 rounded"
          placeholder="Password"
          value={password}
          autoComplete="new-password"
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button
          type="submit"
          disabled={isLoading}
          className="w-full bg-indigo-600 text-white py-2 rounded hover:bg-indigo-700 transition disabled:opacity-60 disabled:cursor-not-allowed"
        >
          {isLoading ? "Creating account..." : "Create Account"}
        </button>

        <p className="mt-4 text-center text-sm text-gray-500">
          Already have an account?{" "}
          <span
            className="text-indigo-600 cursor-pointer hover:underline"
            onClick={() => navigate("/login/email")}
          >
            Sign in
          </span>
        </p>
      </form>
    </div>
  );
};

export default RegisterPage;