import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const RegisterPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");

  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      await axios.post("/api/v1/auth/register", {
        email,
        password,
        username
      });

      navigate("/login/email");
    } catch (err) {
      console.error(err);
      alert("Registration failed");
    }
  };

  return (
    <div className="flex items-center justify-center">
      <div className="bg-white p-8 shadow rounded w-96">
        <h2 className="text-xl font-bold mb-4">Create Account</h2>

        <input
          className="border w-full mb-3 p-2"
          placeholder="Username"
          value={username}
          onChange={(e)=>setUsername(e.target.value)}
        />

        <input
          className="border w-full mb-3 p-2"
          placeholder="Email"
          value={email}
          onChange={(e)=>setEmail(e.target.value)}
        />

        <input
          type="password"
          className="border w-full mb-3 p-2"
          placeholder="Password"
          value={password}
          onChange={(e)=>setPassword(e.target.value)}
        />

        <button
          onClick={handleRegister}
          className="w-full bg-indigo-600 text-white py-2 rounded"
        >
          Create Account
        </button>
      </div>
    </div>
  );
};

export default RegisterPage;
